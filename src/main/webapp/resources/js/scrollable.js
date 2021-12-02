var ftitle = document.querySelector('.filter-title');
var radiocont = document.querySelector('.radio-container');

var w = ftitle.clientWidth;
var h = radiocont.clientHeight;

if (h > w) { ftitle.style.width = h + 'px';}