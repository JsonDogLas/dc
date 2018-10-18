var timeRange = {
    start:null,
    end:null
}
$(function(){
    layui.use(['laydate', 'element', 'layer'], function () {
        $ = layui.jquery;//jquery
        laydate = layui.laydate;//日期插件
        layer = layui.layer;//弹出层

        timeRange.start = {
            min: laydate.now()
            , max: '2099-06-16 23:59:59'
            , istoday: false
            , choose: function (datas) {
                timeRange.end.min = datas; //开始日选好后，重置结束日的最小日期
                timeRange.end.start = datas //将结束日的初始值设定为开始日
            }
        };

        timeRange.end = {
            min: laydate.now()
            , max: '2099-06-16 23:59:59'
            , istoday: false
            , choose: function (datas) {
                timeRange.start.max = datas; //结束日选好后，重置开始日的最大日期
            }
        };
        $("#range_start").click(function(){
            timeRange.start.elem = this;
            laydate(timeRange.start);
        })
        $("#range_end").click(function(){
            timeRange.end.elem = this
            laydate(timeRange.end);
        })

    });

})
