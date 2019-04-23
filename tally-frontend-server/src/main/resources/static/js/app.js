if (!Date.prototype.toInputString) {
  (function() {

    function pad(number) {
      if (number < 10) {
        return '0' + number;
      }
      return number;
    }

    Date.prototype.toInputString = function() {
      return this.getUTCFullYear() +
        '-' + pad(this.getUTCMonth() + 1) +
        '-' + pad(this.getUTCDate());
    };

  }());
}

document.addEventListener('DOMContentLoaded', () => {
    document.querySelectorAll('.is-date-utc').forEach(dateField => {
        const dateUtc = new Date(dateField.textContent + "Z");
        dateField.textContent = dateUtc.toLocaleDateString();
    });

    Chart.defaults.global.legend.display=false;
    Chart.defaults.global.tooltips.enabled=false;
    Chart.defaults.global.scales = {
        yAxes: [{
            type: "linear",
            stacked: true,
            display: false,
            scaleLabel: {
                display: false
            },
            gridLines: {
                display: false
            },
            ticks: {
                display: false
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

    const nowString = new Date().toInputString();
    document.querySelectorAll('input[type=date]').forEach(dateInput => {
        dateInput.value = nowString;
    });
});