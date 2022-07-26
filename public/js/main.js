(function (requirejs) {
  'use strict';

  requirejs.config({
    paths: {
      "jquery": "../lib/jquery/jquery",
      "underscore": "../lib/underscorejs/underscore",
      "d3": "../lib/d3js/d3",
      "nvd3": "../lib/nvd3/nv.d3",
      "bootstrap": "../lib/bootstrap/js/bootstrapx"
    },

    shim: {
      nvd3: {
        deps: ["d3.global"],
        exports: "nv"
      },
      bootstrap: { deps: ["jquery"]}
    }
  });
})(requirejs);

define("d3.global", ["d3"], function(d3global) {
  d3 = d3global;
});

require([], function() {
  console.log("hello, JavaScript");
 });``