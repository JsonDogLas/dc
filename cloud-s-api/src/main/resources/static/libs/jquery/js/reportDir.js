/**
 * Glacier
 */
(function($) {
	'use strict';
	$.reportDir = function(el, options) {
		if (!(this instanceof $.reportDir)) {
			return new $.reportDir(el, options);
		}
		var self = this;
		var template = options.template;
		if(template && (template.substring(0,1) == "#" ||
				template.substring(0,1)==".")){
			template = $(template).html()
		}
		self.$container = $(el);
		self.$container.data('reportDir', self);
		
		self.init = function() {
			self.options = $.extend({}, $.reportDir.defaultOptions, options);
			self.extendJquery();
			self.$container.rList = new $.jqList($(el), self.options);
		};
		
		self.extendJquery = function() {
			$.fn.jqListHTML = function(s) {
				return s ? this.before(s).remove() : $('<p>').append(
						this.eq(0).clone()).html();
			};
		};
		self.search = function(options){
			var $key = options.key;
		    if(isNullOrEmpty($key.val()) && options.type=="search"){
		    	message.alert("搜索时，关键字不能为空");
		        self.$container.rList.jqList('query');
		        return false;
		    }
		    var $imgPath = options.imgPath;
		    var path = $.reportDir.getQueryPath(options);
		    var r = path.split("#.#");
		    var data = {
		        "key": function () {
		            return $key.val();
		        },
		        "path": r[0],
		        "type": r[1],
		        "createUser":options.createUser,
		        //用于显示签名文件，只在第一级目录下显示
		        "firstInto":options.firstInto
		    };
		    self.$container.rList.jqList('query', {"data": data,currentPage:options.currentPage});
		};
		self.query = function(options) {
			self.search(options);
		};
		self.into = function(options){
			self.search(options);
		};
		self.home = function(options){
			self.search(options);
		};
		self.up = function(options){
			self.search(options);
		};
		self.callMethod = function(method, options) {
			switch (method) {
			case 'query':
				if(options != null){
					self.options = $.extend({}, self.options, options);
				}
				self.query(self.options);
				break;
			case 'search':
				self.search(options);
				break;
			case 'openFile':
				self.openFile(options);
				break;
			case 'openDir':
				self.openDir(options);
				break;
			case 'home':
				self.home(options);
				break;
			case 'up':
				self.up(options);
				break;
			case 'empty':
				self.$container.rList.find(".list").empty();
				break;
			case 'destroy':
				if(self.$container.rList){
					self.$container.rList.empty();
				}
				break;
			default:
				throw new Error('[reportDir] method "' + method
						+ '" does not exist');
			}
			return self.$container;
		};
		
		self.init();
		return self.$container;
	};

	$.fn.reportDir = function() {
		var self = this, args = Array.prototype.slice.call(arguments);
		if (typeof args[0] === 'string') {
			var $instance = $(self).data('reportDir');
			if (!$instance) {
				throw new Error('[reportDir] the element is not instantiated');
			} else {
				return $instance.callMethod(args[0], args[1]);
			}
		} else {
			return new $.reportDir(this, args[0]);
		}
	};
	$.reportDir.getQueryPath = function(options){
	    var path = options.imgPath.val();
	    var $key = options.key;
	    var dirPath = options.dirPath;
	    var dirId = "";
	    var type = options.type;
	    if(path == ""){
	    	options.firstInto = "1";
	    }else{
	    	options.firstInto = "0";
	    }
	    if (type == "search") {
	        path = "key@" + $key.val();
	        dirId = $key.val();
	    }
	    else if (type == "into") {
	        if (isNullOrEmpty(dirPath)) {
	        	message.alert("无效目录");
	            return "";
	        }
	        path = dirPath + "|" + path;
	        type = "getFolder";
	        dirId = dirPath;
	    }
	    else if (type == "up") {
	        if (isNullOrEmpty(path)) {
	            path = "";
	            type = "home";
	            dirId = "";
	        }
	        else {
	            var r = path.split('|');
	            path = path.replace(r[0] + "|", ""); //返回一级
	            r = path.split('|');
	            if (r[0].indexOf('key@') >= 0) {
	                //如果第一个是关键查询
	                path = r[0];
	                type = "search";
	                dirId = $key.val();
	            }
	            else {
	                if (!isNullOrEmpty(r[0])) {
	                    type = "getFolder";
	                    dirId = r[0];
	                }
	                else {
	                    path = "";
	                    type = "home";
	                    dirId = "";
	                }
	            }
	        }
	        if(r&&r.length ==2){
		    	options.firstInto = "1";
		    }else{
		    	options.firstInto = "0";
		    }
	    }
	    else if (type == "home") {
	        path = "";
	        dirId = "";
	    }
	    options.imgPath.val(path);
	    return dirId + "#.#" + type;
	};
})(jQuery);
