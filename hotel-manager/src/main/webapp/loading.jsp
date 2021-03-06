<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<!--
THIS EXAMPLE WAS DOWNLOADED FROM https://echarts.apache.org/examples/zh/editor.html?c=graphic-stroke-animation
-->
<!DOCTYPE html>
<html style="height: 100%">
<head>
    <meta charset="utf-8">
    <title>正在加载中...</title>
</head>
<body style="height: 100%; margin: 0">
<div id="container" style="height: 100%"></div>


<script type="text/javascript" src="${pageContext.request.contextPath}/static/echarts/echarts.js"></script>

<script type="text/javascript">
    var dom = document.getElementById("container");
    var myChart = echarts.init(dom);
    var app = {};

    var option;

    option = {
        graphic: {
            elements: [
                {
                    type: 'text',
                    left: 'center',
                    top: 'center',
                    style: {
                        text: 'Welcome To Hotel Manager',
                        fontSize: 80,
                        fontWeight: 'bold',
                        lineDash: [0, 200],
                        lineDashOffset: 0,
                        fill: 'transparent',
                        stroke: '#000',
                        lineWidth: 1
                    },
                    keyframeAnimation: {
                        duration: 3000,
                        loop: true,
                        keyframes: [
                            {
                                percent: 0.7,
                                style: {
                                    fill: 'transparent',
                                    lineDashOffset: 200,
                                    lineDash: [200, 0]
                                }
                            },
                            {
                                // Stop for a while.
                                percent: 0.8,
                                style: {
                                    fill: 'transparent'
                                }
                            },
                            {
                                percent: 1,
                                style: {
                                    fill: 'black'
                                }
                            }
                        ]
                    }
                }
            ]
        }
    };


    if (option && typeof option === 'object') {
        myChart.setOption(option);
    }


    setTimeout(() => {
        window.location.href = "/index.jsp";
    },3000)

</script>
</body>
</html>
