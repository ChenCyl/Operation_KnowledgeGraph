<template>
    <el-container>
        <el-row style="width:100%;height:100%">
            <el-col class="box2">
                <el-col style="width:80px;">
                    <el-checkbox :indeterminate="isIndeterminate" v-model="checkAll" @change="handleCheckAllChange">全选</el-checkbox>
                    <!-- <div style="margin: 15px 0;"></div> -->
                    <el-checkbox-group v-model="checkedCities" @change="handleCheckedCitiesChange">
                        <el-checkbox v-for="city in cities" :label="city" :key="city" style="margin-top:15px;">{{city}}</el-checkbox>
                    </el-checkbox-group>
                </el-col>
            </el-col>

            <el-col style="width:60%;height:600px;margin-left:8%;margin-top:30px;">
                <div refs="chart" id="chart" style="width:100%;height:100%"></div>
            </el-col>
        </el-row>

    </el-container>
</template>

<style scoped>
    .el-checkbox+.el-checkbox {
        margin-left: 0px;
    }

    .box2 {
        margin-top: 30px;
        margin-left: 50px;
        /* margin: 20px auto; */
        width: 200px;
        min-height: 500px;
        padding: 10px;
        position: relative;
        background: -webkit-gradient(linear, 0% 20%, 0% 92%, from(#c7ecf59a), to(rgb(236, 246, 248)), color-stop(.1, #c7ecf59a));
        border-top: 1px solid #ccc;
        border-right: 1px solid #ccc;
        -webkit-border-bottom-right-radius: 60px 60px;
        -webkit-box-shadow: -1px 2px 2px rgba(0, 0, 0, 0.2);
    }

    .box2:before {
        content: '';
        width: 25px;
        height: 20px;
        position: absolute;
        bottom: 0;
        right: 0;
        -webkit-border-bottom-right-radius: 30px;
        -webkit-box-shadow: -2px -2px 5px rgba(0, 0, 0, 0.3);
        -webkit-transform:
            rotate(-20deg) skew(-40deg, -3deg) translate(-13px, -13px);
    }
</style>

<script>
    const echarts = require('echarts');
    const cityOptions = ['Data1', 'Data2', 'Data3', 'Data4', 'Data5'];

    var option = {
        title: {
            text: 'TestDiagram'
        },
        tooltip: {
            trigger: 'axis'
        },
        legend: {
            data: ['Data1', 'Data2', 'Data3', 'Data4', 'Data5']
        },
        grid: {
            left: '3%',
            right: '4%',
            bottom: '3%',
            containLabel: true
        },
        toolbox: {
            feature: {
                saveAsImage: {}
            }
        },
        xAxis: {
            type: 'category',
            boundaryGap: false,
            data: ['Data1', 'Data2', 'Data3', 'Data4', 'Data5', 'Data6', 'Data7']
        },
        yAxis: {
            type: 'value'
        },
        series: [{
                name: 'Data1',
                type: 'line',
                stack: '总量',
                data: [120, 132, 101, 134, 90, 230, 210]
            },
            {
                name: 'Data2',
                type: 'line',
                stack: '总量',
                data: [220, 182, 191, 234, 290, 330, 310]
            },
            {
                name: 'Data3',
                type: 'line',
                stack: '总量',
                data: [150, 232, 201, 154, 190, 330, 410]
            },
            {
                name: 'Data4',
                type: 'line',
                stack: '总量',
                data: [320, 332, 301, 334, 390, 330, 320]
            },
            {
                name: 'Data5',
                type: 'line',
                stack: '总量',
                data: [820, 932, 901, 934, 1290, 1330, 1320]
            }
        ]
    };
    export default {
        name: 'series',
        data() {
            return {
                checkAll: false,
                checkedCities: ['Data1', 'Data2', 'Data3', 'Data4', 'Data5'],
                cities: cityOptions,
                isIndeterminate: false,
                myChart: '',
                series: [{
                        name: 'Data1',
                        type: 'line',
                        stack: '总量',
                        data: [120, 132, 101, 134, 90, 230, 210]
                    },
                    {
                        name: 'Data2',
                        type: 'line',
                        stack: '总量',
                        data: [220, 182, 191, 234, 290, 330, 310]
                    },
                    {
                        name: 'Data3',
                        type: 'line',
                        stack: '总量',
                        data: [150, 232, 201, 154, 190, 330, 410]
                    },
                    {
                        name: 'Data4',
                        type: 'line',
                        stack: '总量',
                        data: [320, 332, 301, 334, 390, 330, 320]
                    },
                    {
                        name: 'Data5',
                        type: 'line',
                        stack: '总量',
                        data: [820, 932, 901, 934, 1290, 1330, 1320]
                    }
                ]
            };
        },
        methods: {
            chartUpdate(value) {
                let currentSeries = []
                this.series.forEach(serie => {
                    value.forEach(value => {
                        if (serie.name == value) {
                            currentSeries.push(serie)
                        }
                    });
                });

                option.series = currentSeries;

                this.myChart.setOption(option, {
                    notMerge: true
                });
            },
            handleCheckAllChange(val) {
                this.checkedCities = val ? cityOptions : [];
                this.isIndeterminate = false;

                this.chartUpdate(this.checkedCities);
            },
            handleCheckedCitiesChange(value) {
                let checkedCount = value.length;
                this.checkAll = checkedCount === this.cities.length;
                this.isIndeterminate = checkedCount > 0 && checkedCount < this.cities.length;

                this.chartUpdate(value);
            }
        },
        mounted() {
            this.checkAll = true;
            this.myChart = echarts.init(document.getElementById('chart'));
            // var myChart = echarts.init(this.$refs.chart);
            this.myChart.setOption(option);

        }

    }
</script>