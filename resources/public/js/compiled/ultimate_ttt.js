if(typeof goog == "undefined") document.write('<script src="js/compiled/out-dev/goog/base.js"></script>');
document.write('<script src="js/compiled/out-dev/cljs_deps.js"></script>');
document.write('<script>if (typeof goog != "undefined") { goog.require("ultimate_ttt.core"); } else { console.warn("ClojureScript could not load :main, did you forget to specify :asset-path?"); };</script>');
