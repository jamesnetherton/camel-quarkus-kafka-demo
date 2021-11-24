const data = {
  datasets: [{
    label: 'USD / EUR',
    backgroundColor: 'rgb(255, 99, 132)',
    borderColor: 'rgb(255, 99, 132)',
  }]
};

const config = {
  type: 'line',
  data: data,
  options: {}
};

const myChart = new Chart(
    document.getElementById('chart'),
    config
);

fetchExchangeRates();

setInterval(function() {
  fetchExchangeRates();
}, 5000);

function fetchExchangeRates() {
  $.get("/exchangerates", function(data) {
    const labels = [];
    const dataset = [];
    for (let i = 0; i < data.length; i++) {
        const date = new Date(data[i].timestamp * 1000);
        labels.push(formatTimeSegment(date.getHours()) + ":" + formatTimeSegment(date.getMinutes()) + ":" + formatTimeSegment(date.getSeconds()));
        dataset.push("0." + data[i].value);
    }

    myChart.data.labels = labels;
    myChart.data.datasets[0].data = dataset;
    myChart.update();
  });
}

function formatTimeSegment(segment) {
    return ('0' + segment).slice(-2);
}