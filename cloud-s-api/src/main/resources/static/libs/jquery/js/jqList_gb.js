/**
 * Glacier
 */
(function($) {
	'use strict';
	$.jqList = function(el, options) {
		if (!(this instanceof $.jqList)) {
			return new $.jqList(el, options);
		}
		var self = this;
		var template = options.template;
		if(template && (template.substring(0,1) == "#" ||
				template.substring(0,1)==".")){
			template = $(template).html()
		}
		self.$container = $(el);
		//
		self.$container.data('jqList', self);
		self.$container.find(".header > .sortable").each(function(){
			//sortable
			var $item = $(this);
			var $direction = $item.find(".direction");
			if($direction.length <= 0){
				$direction = $("<img class=\"direction\" />");
				$direction.attr("src",contextPath + "rms/resources/sort/sort.gif");
				$item.append($direction);
			}
			$item.bind("click",function(){
				self.orderBy(this);
			})
		});
		var $list = self.$container.find(".list");
		self.init = function() {
			self.options = $.extend({}, $.jqList.defaultOptions, options);
			options.totalPages = 1;
			options.currentPage = 1;
			options.onPageChange = function(num, type){
				//$('#text').html('当前第' + num + '页');
				//alert(num);
				//alert(options.currentPage);
				if(self.options.currentPage != num){
					self.options.currentPage = num;
					self.query(self.options);
					if(typeof(self.options.onPageChange) == "function"){
						self.options.onPageChange(num, type);
					}
				}
			}
			
			//
			juicer.set('cache', true);
			juicer.set('errorhandling', false);
			juicer.set('strip', true);
			//juicer.set('detection', false);
			//
			//self.verify();
			self.extendJquery();
			//self.query(self.options);
		};
		self.verify = function() {
			//
		};
		self.extendJquery = function() {
			$.fn.jqListHTML = function(s) {
				return s ? this.before(s).remove() : $('<p>').append(
						this.eq(0).clone()).html();
			};
		};
		self.render = function(data) {
			self.renderHtml(data);
			// self.setStatus();
			self.bindEvents();
		};
		self.renderHtml = function(data) {
			if(data){
				//$list.html("");
				//debugger;
				var html;
				var total = data.total;
				var totalPages = Math.ceil(total / self.options.pageSize);
				if(self.$paginator == null){
					self.$paginator = self.$container.find('.pagination');
					options.totalPages = totalPages;
					options.totalCounts = total;
					options.visiblePages = 6;
					if(self.$paginator.length > 0){
						self.$paginator.jqPaginator(options);
					}
				}else if(self.options.currentPage && self.options.currentPage <= 1){
					if(self.$paginator.length > 0){
						self.$paginator.jqPaginator('option', {
							totalPages: Math.ceil(total / self.options.pageSize),
							totalCounts:total,
							currentPage:self.options.currentPage
						});
					}
				}
				//
				if(data.rows){
					var arr = [],isRender,result;
					for(var i =0;i<data.rows.length;i++){
						var row = data.rows[i];
						//var html = null;
						isRender = false;
						if(typeof self.options.onRowRending == 'function'){
							result = self.options.onRowRending(row);
							if(result != null){
								arr[arr.length] = result;
								//html = result;
								isRender = true;
							}
						}
						if(!isRender){
							arr[arr.length] = juicer(template, row);
							//html = juicer(template, row);
						}
						if(typeof self.options.onRowRended == 'function'){
							if(self.options.onRowRended(row) === false){
								return;
							}
						}
						//var $html = $(html);
						//if($html.length > 0){
						//	$html[0].row = row;
						//}
						//$list.append($html);
					}
					//将之前已在list中的数据添加到html
					if(typeof(self.options.onPhoneMore) == "function"){
						html = self.options.onPhoneMore(self,arr,totalPages);
					}else{
						html = arr.join("");
					}
				}else{
					html= options.emptyString;
					//$list.append(options.emptyString);
				}
			}
			$list.html(html);
		};
		self.getRow = function(id){
			if(self.data && self.data.rows){
				var rows = self.data.rows
				for(var i=0;i<rows.length;i++){
					if(rows[i]["ID"] == id){
						return rows[i];
					}
				}
			}
		}
		self.query = function(options) {
			if(!options.currentPage){
				options.currentPage = 1;
			}
			// var start = (options.currentPage-1)*options.pageSize;
			// var url = options.url + "&start=" + start + "&limit=" + options.pageSize;
			// if(options.orderBy != null){
			// 	url += "&orderBy=" + options.orderBy;
			// }
            var params = {
                page:options.currentPage,
                pageSize:options.pageSize,
				//排序字段
                sidx:function(){
                    if(options.orderBy != null){
                        return options.orderBy;
                    }
                    return "";
                },
                //搜索字段
                searchField:function(){
                    if(options.data && options.data.searchField != null){
                        return options.data.searchField;
                    }
                    return "";
                },
				//搜索值
                searchString:function(){
                    if(options.data && options.data.searchString != null){
                        return options.data.searchString;
                    }
                    return "";
                }

            };

			for(var key in params){
				var value=params[key];
				if(typeof value == "function"){
                    var test=value();
                    if(test == null || test == ""){
                    	continue;
					}else{
                    	params[key]=test;
					}
				}
			}

			if(typeof options.data == "object"){
				var requestParam=options.data;
				for(var item in requestParam){
                    params[item]=requestParam[item];
                }
			}

			$.ajax({
				type : 'POST',
				url : options.url,
				data : JSON.stringify(params),
                contentType:"application/json",
				dataType : "json",
				beforeSend:function(){
					//开启菊花加载
					if(typeof self.options.onStartSpin == 'function'){
						self.options.onStartSpin();
					}
				},
				success : function(data) {
					self.data = data;
					self.verify();
					self.render(data);
					if(typeof self.options.onCompleted == 'function'){
						self.options.onCompleted(true,data);
					}
					//关闭菊花加载
					if(typeof self.options.onEndSpin == 'function'){
						self.options.onEndSpin();
					}
				},
				error : function(jqXHR, textStatus, errorThrown) {
					//alert("error");
					//清空列表数据
					self.$container.find(".list").html('');
					//关闭菊花加载
					if(typeof self.options.onEndSpin == 'function'){
						self.options.onEndSpin();
					}
					if(typeof self.options.onError == 'function'){
						self.options.onError(textStatus);
					}
					if(typeof self.options.onCompleted == 'function'){
						self.options.onCompleted(false);
					}
				}
			});
		};

		self.fireEvent = function(row, type) {
			return (typeof self.options.onPageChange !== 'function')
					|| (self.options.onPageChange(row, type) !== false);
		};
		self.orderBy = function(e) {
			//debugger;
			var $e = $(e);
			var field = $e.attr("field");
			var direction = $e.attr("direction");
			var $direction = $e.find(".direction");
			if(direction == null){
				$direction.attr("src",contextPath + "rms/resources/sort/sort_asc.gif");
				$e.attr("direction","asc");
				self.options["orderBy"] = field + " asc";
			}else if(direction == "asc"){
				$direction.attr("src",contextPath + "rms/resources/sort/sort_desc.gif");
				$e.attr("direction","desc");
				$direction.html("<");
				self.options["orderBy"] = field + " desc";
			}else{
				delete self.options["orderBy"];
				$e.attr("direction",null);
				$direction.attr("src",contextPath + "rms/resources/sort/sort.gif");
			}
			/*self.options.currentPage = 1;
			options.currentPage = 1;
			options.totalPages = self.options.totalPages;
			options.totalCounts = self.options.total;
			if(self.$paginator.length > 0){
				self.$paginator.jqPaginator(options);
			}*/
			self.query(self.options);
			//alert(self.options["orderBy"]);
			//return orderBy;
		};
		self.callMethod = function(method, options) {
			switch (method) {
			case 'options':
				self.options = $.extend({}, self.options, options);
				break;
			case 'query':
				if(options != null){
					self.options = $.extend({}, self.options, options);
				}
				self.query(self.options);
				break;
			case 'orderBy':
				self.orderBy(options);//options elemen
				break;
			case 'getRow':
				return self.getRow(options);
			case 'destroy':
				self.$container.empty();
				self.$container.removeData('jqList');
				break;
			default:
				throw new Error('[jqList] method "' + method
						+ '" does not exist');
			}
			return self.$container;
		};
		self.bindEvents = function() {
			var opts = self.options;
			self.$container.off();
			self.$container.on('click', '[jp-role]', function() {
				var $el = $(this);
				if ($el.hasClass(opts.disableClass)
						|| $el.hasClass(opts.activeClass)) {
					return;
				}
				var pageIndex = +$el.attr('jp-data');
				if (self.fireEvent(pageIndex, 'change')) {
					var total = self.data.total;
					var totalPages = Math.ceil(total / self.options.pageSize);
					if(totalPages > pageIndex && self.options.currentPage != pageIndex && pageIndex !=0){
						self.switchPage(pageIndex);
					}
				}
			});
		};
		self.init();
		return self.$container;
	};

	$.jqList.defaultOptions = {
			//
	};
	$.fn.jqList = function() {
		var self = this, args = Array.prototype.slice.call(arguments);
		if (typeof args[0] === 'string') {
			var $instance = $(self).data('jqList');
			if (!$instance) {
				throw new Error('[jqList] the element is not instantiated');
			} else {
				return $instance.callMethod(args[0], args[1]);
			}
		} else {
			return new $.jqList(this, args[0]);
		}
	};
})(jQuery);
//全局--列表中统一显示6条
var pageCount = 6;