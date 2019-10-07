import {registerPrototype} from './utc';

registerPrototype();

document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.is-date-utc').forEach(dateField => {
        const dateUtc = new Date(dateField.textContent + "Z");
        dateField.textContent = dateUtc.toLocaleDateString();
    });

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