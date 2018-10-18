/**
 * 拖曳控件
 * options
 * 回调函数
 * moveBegin 移动开始
 * 		moveDom 移动dom
 * moveEnd 移动结束
 * 		moveDom 移动dom
 * ismovein 正在移动 ()   
 * 		参数
 * 		moveDom 移动dom
 *		top 下一次要移动的绝对位置
 *		left 下一次要移动的绝对位置
 *
 *		return
 *		boolean
 *		true 移动 false 不移动
 * offsetChange 可要可不要
 */
$.fn.drag=function(options){
		//系统默认移动
		var ismovein=function(moveDom,top,left){
			return true;
		}
		if(options.ismovein == null || typeof(options.ismovein) != "function"){
        	options.ismovein=ismovein;
        }
    	//鼠标左键按下
    	$(this).bind("mousedown",function(event){
    		console.log($(this).parent().html());
    		//console.log("thisele.left:"+$(this).offset().left+"  thisele.top"+$(this).offset().top);
    		var canmove = true;//已经按下
            var disX=event.pageX-$(this).offset().left;
            var disY=event.pageY-$(this).offset().top;
            //console.log("disX:"+disX+"  disY"+disY);

            var thisele=$(this);
            thisele.unbind("mousemove");
            //移动结束
            var moveEnd=function(){
            	$(document).unbind("mousemove");
                $(document).unbind("mouseup");
                $(this).unbind("mouseup");
                if(options.moveEnd != null && typeof(options.moveEnd) == "function"){
                	options.moveEnd(thisele);
                }
            }
            //移动开始
            if(options.moveBegin != null && typeof(options.moveBegin) == "function"){
            	options.moveBegin(thisele);
            }
            $(document).bind("mousemove",function(event){
                //鼠标按下才能移动
            	if(!canmove){
            		return false;
            	}
            	//console.log("event.pageY:"+event.pageY+"  event.pageX"+event.pageX);
            	var top=event.pageY - disY;
                var left=event.pageX - disX;
                //console.log("top:"+top+"  left"+left);
                //console.log("----------------------------------");
            	//回调是否移动
                if(options.ismovein(thisele,top,left,options.offsetChange)){
                	thisele.css("top",top+"px");
                	thisele.css("left",left+"px");
                	thisele.css("position","absolute");
                }
                
                //当鼠标离开 时 解除绑定事件
                var clientY=event.pageY;
                var clientX=event.pageX;
                if(!(thisele.offset().top<clientY && clientY <(thisele.offset().top+thisele.height()) &&  thisele.offset().left<clientX && clientX <(thisele.offset().left+thisele.width()))){
                	moveEnd();
                }
                
                //返回false阻止事件冒泡  不让 其他的节点被 拖曳选中 其他节点被选中之后就会影响拖曳
                return false;
            });
            //鼠标左键抬起
        	$(document).bind("mouseup",function(){
        		moveEnd();
        		canmove = false;//已经抬起
            });
        	$(this).bind("mouseup",function(){
        		moveEnd();
        		canmove = false;//已经抬起
        	});
    	});
}