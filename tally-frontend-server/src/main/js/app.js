import { registerPrototype, utcToLocal } from './utc';
import { DateLabel } from './date-label';

registerPrototype();
customElements.define("date-label", DateLabel, { extends: 'time' });

document.addEventListener('DOMContentLoaded', () => {
    const nowString = new Date().toInputString();
    document.querySelectorAll('input[type=date]').forEach(dateInput => {
        dateInput.value = nowString;
    });
    
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