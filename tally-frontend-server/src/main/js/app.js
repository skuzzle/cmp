import { DateLabel } from './date-label';
import { InputNow } from './input-now';

customElements.define("date-label", DateLabel, { extends: 'time' });
customElements.define("input-now", InputNow, { extends: 'input' });

document.addEventListener('DOMContentLoaded', () => {
   
    Chart.defaults.global.legend.display=false;
    Chart.defaults.global.tooltips.enabled=false;
    Chart.defaults.global.scales = {
        yAxes: [{
            type: "linear",
            stacked: true,
            display: true,
            scaleLabel: {
                display: false
            },
            gridLines: {
                display: false
            },
            ticks: {
                display: true,
                precision: 0,
                stepSize: 1
            }
        }],
    };

    document.querySelectorAll('.is-tally-graph').forEach(canvas => {
        new Chart(canvas, {
            type: 'line',
            data: GRAPH_DATA,
            options: {
                scales: {
                    xAxes: [{
                        type: "linear",
                        scaleLabel: {
                            display: false
                        },
                        gridLines: {
                            display: false,
                            drawTicks: false
                        },
                        ticks: {
                            display: false
                        }
                    }]
                }
            }
        });
    });

});