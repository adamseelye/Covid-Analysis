const chart = document.getElementById("barchart")
const context = chart.getContext("2d")
const statseries = JSON.parse(chart.dataset.statseries)
const stat = chart.dataset.stat
const countryseries = JSON.parse(chart.dataset.countryseries)
const myChart = new Chart(context, {
  type: 'bar',
  data: {
    labels: countryseries,
    datasets: [{
      label: stat,
      data: statseries,
      fill: false,
      backgroundColor: 'rgba(255, 0, 0, 1)'
    }]
  },
  options: {
    indexAxis: 'y',
    responsive: true,
  }
});
console.log("Bar Graph Ran");