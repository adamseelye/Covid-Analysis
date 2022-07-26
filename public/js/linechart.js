const chart = document.getElementById("linechart")
const context = chart.getContext("2d")
const statseries = JSON.parse(chart.dataset.statseries)
const stat = chart.dataset.stat
const dateseries = JSON.parse(chart.dataset.dateseries)
const myChart = new Chart(context, {
  type: 'line',
  data: {
    labels: dateseries,
    datasets: [{
      label: stat,
      data: statseries,
      fill: false,
      borderColor: [
        'rgba(255, 99, 132, 1)'
      ]
    }]
  }
});
console.log("Line Graph Ran");