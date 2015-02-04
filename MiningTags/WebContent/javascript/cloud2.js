var fill = d3.scale.category20b();

var w = 475,
	h = 350;

var words = [],
    max,
    scale = 1,
    complete = 0,
    keyword = "",
    tags,
    fontSize,
    maxLength = 30,
    fetcher,
    statusText = d3.select("#status");
	highlightflag = 0;//判断是否高亮
	highlightkey = "";//高亮关键字
var layout2 = d3.layout.cloud()
    .timeInterval(10)
    .size([w, h])
    .fontSize(function(d) {return d.value; })
    .text(function(d) { return d.key; })
    .on("word", progress)
    .on("end", draw2);

var svg = d3.select("#vos").append("svg")
    .attr("width", w)
    .attr("height", h);

var background = svg.append("g"),
    vis = svg.append("g")
    .attr("transform", "translate(" + [w >> 1, h >> 1] + ")");

d3.select("#download-svg").on("click", downloadSVG);
d3.select("#download-png").on("click", downloadPNG);

d3.select(window).on("hashchange", hashchange);


form.selectAll("input[type=number]")
    .on("click.refresh", function() {
	  if(!names) return;
      if (this.value === this.defaultValue) return;
      generate2();
      this.defaultValue = this.value;
    });
form.selectAll("input[type=radio], #font")	
    .on("change", generate);

// From Jonathan Feinberg's cue.language, see lib/cue.language/license.txt.
var stopWords = /^(i|me|my|myself|we|us|our|ours|ourselves|you|your|yours|yourself|yourselves|he|him|his|himself|she|her|hers|herself|it|its|itself|they|them|their|theirs|themselves|what|which|who|whom|whose|this|that|these|those|am|is|are|was|were|be|been|being|have|has|had|having|do|does|did|doing|will|would|should|can|could|ought|i'm|you're|he's|she's|it's|we're|they're|i've|you've|we've|they've|i'd|you'd|he'd|she'd|we'd|they'd|i'll|you'll|he'll|she'll|we'll|they'll|isn't|aren't|wasn't|weren't|hasn't|haven't|hadn't|doesn't|don't|didn't|won't|wouldn't|shan't|shouldn't|can't|cannot|couldn't|mustn't|let's|that's|who's|what's|here's|there's|when's|where's|why's|how's|a|an|the|and|but|if|or|because|as|until|while|of|at|by|for|with|about|against|between|into|through|during|before|after|above|below|to|from|up|upon|down|in|out|on|off|over|under|again|further|then|once|here|there|when|where|why|how|all|any|both|each|few|more|most|other|some|such|no|nor|not|only|own|same|so|than|too|very|say|says|said|shall)$/,
    punctuation = new RegExp("[" + unicodePunctuationRe + "]", "g"),
    wordSeparators = /[\s\u3031-\u3035\u309b\u309c\u30a0\u30fc\uff70]+/g,
    discard = /^(@|https?:|\/\/)/,
    htmlTags = /(<[^>]*?>|<script.*?<\/script>|<style.*?<\/style>|<head.*?><\/head>)/g,
    matchTwitter = /^https?:\/\/([^\.]*\.)?twitter\.com/;


function parseHTML2(d) {
  parseText2(d.replace(htmlTags, " ").replace(/&#(x?)([\dA-Fa-f]{1,4});/g, function(d, hex, m) {
    return String.fromCharCode(+((hex ? "0x" : "") + m));
  }).replace(/&\w+;/g, " "));
}

function getURL2(url, callback) {
  statusText.text("Fetching… ");

  if (matchTwitter.test(url)) {
    var iframe = d3.select("body").append("iframe").style("display", "none");
    d3.select(window).on("message", function() {
      var json = JSON.parse(d3.event.data);
      callback((Array.isArray(json) ? json : json.results).map(function(d) { return d.text; }).join("\n\n"));
      iframe.remove();
    });
    iframe.attr("src", "http://jsonp.jasondavies.com/?" + encodeURIComponent(url));
    return;
  }

  try {
    d3.text(url, function(text) {
      if (text == null) proxy2(url, callback);
      else callback(text);
    });
  } catch(e) {
    proxy2(url, callback);
  }
}

function proxy2(url, callback) {
  d3.text("//www.jasondavies.com/xhr?url=" + encodeURIComponent(url), callback);
}

function flatten2(o, k) {
  if (typeof o === "string") return o;
  var text = [];
  for (k in o) {
    var v = flatten2(o[k], k);
    if (v) text.push(v);
  }
  return text.join(" ");
}

function parseText2(text) {
  tags = {};
  var cases = {};
  text.split(",").forEach(function(word) {
    if (discard.test(word)) return;
   // word = word.replace(punctuation, "");
    if (stopWords.test(word.toLowerCase())) return;
    word = word.substr(0, maxLength);
    cases[word.toLowerCase()] = word;
    tags[word = word.toLowerCase()] = (tags[word] || 0) + 1;
  });
  tags = d3.entries(tags).sort(function(a, b) { return b.value - a.value; });
  tags.forEach(function(d) { d.key = cases[d.key]; });
  generate2();
}

function generate2() {
 if(!names) return;
  layout2
      .font(d3.select("#font").property("value"))
      .spiral(d3.select("input[name=spiral]:checked").property("value"));
  fontSize = d3.scale[d3.select("input[name=scale]:checked").property("value")]().range([10, 100]);
  if (tags.length) fontSize.domain([+tags[tags.length - 1].value || 1, +tags[0].value]);
  complete = 0;
  statusText.style("display", null);
  words = [];
  for (var key in tags) {//根据挖掘数据大小重置标签大小
	tags[key].value = mapFontSizes.get(tags[key].key);
  };
  layout2.stop().words(tags.slice(0, max = Math.min(tags.length, +d3.select("#max").property("value")))).start();
}

function progress2(d) {
  statusText.text(++complete + "/" + max);
}

function draw2(data, bounds) {
  statusText.style("display", "none");
  scale = bounds ? Math.min(
      w / Math.abs(bounds[1].x - w / 2),
      w / Math.abs(bounds[0].x - w / 2),
      h / Math.abs(bounds[1].y - h / 2),
      h / Math.abs(bounds[0].y - h / 2)) / 2 : 1;
  words = data;
  var text = vis.selectAll("text")
      .data(words, function(d) { return d.text.toLowerCase(); });
  text.transition()
      .duration(1000)
      .attr("transform", function(d) { return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")"; })
      .style("font-size", function(d) { 
		return d.size + "px"; });
  text.enter().append("text")
      .attr("text-anchor", "middle")
      .attr("transform", function(d) { return "translate(" + [d.x, d.y] + ")rotate(" + d.rotate + ")"; })
      .style("font-size", function(d) {
		return d.size + "px"; })
      /* .on("click", function(d) {
    	var selected = d3.select("#selected").property("value");
    	 if(selected.trim()==''){
    		 selected = d.text;
    	 }else{
    		 var words = selected.split(",");
    		 var flag = true;
			 for (var i = 0; i < words.length; i++) {
				 if (words[i].trim() == d.text) {
					 flag = false;
				 }
			 }
			 if(flag)selected = selected.trim()+","+d.text;
    	 }
    	 d3.select("#selected").property("value",selected);
    	 alert(d.style);
      })*/
      .style("opacity", 1e-6)
    .transition()
      .duration(1000)
      .style("opacity", 1);
  text.style("font-family", function(d) { return d.font; })
      .style("fill", function(d) { return fill(d.text.toLowerCase()); })
      .text(function(d) { return d.text; });
  var exitGroup = background.append("g")
      .attr("transform", vis.attr("transform"));
  var exitGroupNode = exitGroup.node();
  text.exit().each(function() {
    exitGroupNode.appendChild(this);
  });
  exitGroup.transition()
      .duration(1000)
      .style("opacity", 1e-6)
      .remove();
  vis.transition()
      .delay(1000)
      .duration(750)
      .attr("transform", "translate(" + [w >> 1, h >> 1] + ")scale(" + scale + ")");
	  
//////add mouseup mouseout event
	if(!refresh){
		$("text").each(function(i){
			if(!$(this).attr('style')) return true;
			
			
			$(this).mouseover(function(){//在每一个匹配元素的mouseover事件中绑定一个处理函数。
					var highlightkey = $(this).text().trim();
					$("text").each(function(i){
						var style = $(this).attr('style');
						var text = $(this).text();
						if(highlights[highlightkey].indexOf(text)==-1){
						 $(this).attr('style',style+"display:none;");
						}
					});
					});
				$(this).mouseout(function(){
					var highlightkey = $(this).text().trim();
					$("text").each(function(i){
						var style = $(this).attr('style');
						var text = $(this).text();
						if(highlights[highlightkey].indexOf(text)==-1){
						 $(this).attr('style',style.substring(0,style.length-13));
						}
					});
			 });
			
			
			$(this).click(function(){//在每一个匹配元素的mouseover事件中绑定一个处理函数。
				var selected = $("#selected").val();
				 if(selected.trim()==''){
					 selected = $(this).text();
				 }else{
					 var words = selected.split(",");
					 var flag = true;
					 for (var i = 0; i < words.length; i++) {
						 if (words[i].trim() == $(this).text()) {
							 flag = false;
						 }
					 }
					 if(flag)selected = selected.trim()+","+$(this).text();
				 }
				 d3.select("#selected").property("value",selected);
			});
		});
	}
}

function hashchange2(fallback) {
  var h = location.hash;
  if (h && h.length > 1) {
    h = decodeURIComponent(h.substr(1));
    if (h !== fetcher) load2(h);
  } else if (fallback) load2(fallback);
}

function load2(f) {
  fetcher = f;
  var h = /^(https?:)?\/\//.test(fetcher)
      ? "#" + encodeURIComponent(fetcher)
      : "";
  if (fetcher != null) d3.select("#text").property("value", fetcher);
  if (location.hash !== h) location.hash = h;
  if (h) getURL2(fetcher, parseHTML);
  else if (fetcher) parseText2(fetcher);
}

d3.select("#random-palette").on("click", function() {
  paletteJSON("http://www.colourlovers.com/api/palettes/random", {}, function(d) {
    fill.range(d[0].colors);
    vis.selectAll("text")
        .style("fill", function(d) { return fill(d.text.toLowerCase()); });
  });
  d3.event.preventDefault();
});

(function() {
  var r = 40.5,
      px = 35,
      py = 20;

  var angles = d3.select("#angles").append("svg")
      .attr("width", 2 * (r + px))
      .attr("height", r + 1.5 * py)
    .append("g")
      .attr("transform", "translate(" + [r + px, r + py] +")");

  angles.append("path")
      .style("fill", "none")
      .attr("d", ["M", -r, 0, "A", r, r, 0, 0, 1, r, 0].join(" "));

  angles.append("line")
      .attr("x1", -r - 7)
      .attr("x2", r + 7);

  angles.append("line")
      .attr("y2", -r - 7);

  angles.selectAll("text")
      .data([-90, 0, 90])
    .enter().append("text")
      .attr("dy", function(d, i) { return i === 1 ? null : ".3em"; })
      .attr("text-anchor", function(d, i) { return ["end", "middle", "start"][i]; })
      .attr("transform", function(d) {
        d += 90;
        return "rotate(" + d + ")translate(" + -(r + 10) + ")rotate(" + -d + ")translate(2)";
      })
      .text(function(d) { return d + "°"; });

  var radians = Math.PI / 180,
      from,
      to,
      count,
      scale = d3.scale.linear(),
      arc = d3.svg.arc()
        .innerRadius(0)
        .outerRadius(r);

  d3.selectAll("#angle-count, #angle-from, #angle-to")
      .on("change", getAngles2)
      .on("mouseup", getAngles2);
	  
  getAngles2();
  function getAngles2() {
    if (!names) return;
    count = +d3.select("#angle-count").property("value");
    from = Math.max(-90, Math.min(90, +d3.select("#angle-from").property("value")));
    to = Math.max(-90, Math.min(90, +d3.select("#angle-to").property("value")));
	update2();
  }

  function update2() {
    scale.domain([0, count - 1]).range([from, to]);
    var step = (to - from) / count;

    var path = angles.selectAll("path.angle")
        .data([{startAngle: from * radians, endAngle: to * radians}]);
    path.enter().insert("path", "circle")
        .attr("class", "angle")
        .style("fill", "#fc0");
    path.attr("d", arc);

    var line = angles.selectAll("line.angle")
        .data(d3.range(count).map(scale));
    line.enter().append("line")
        .attr("class", "angle");
    line.exit().remove();
    line.attr("transform", function(d) { return "rotate(" + (90 + d) + ")"; })
        .attr("x2", function(d, i) { return !i || i === count - 1 ? -r - 5 : -r; });

    var drag = angles.selectAll("http://www.jasondavies.com/wordcloud/path.drag")
        .data([from, to]);
    drag.enter().append("path")
        .attr("class", "drag")
        .attr("d", "http://www.jasondavies.com/wordcloud/M-9.5,0L-3,3.5L-3,-3.5Z")
        .call(d3.behavior.drag()
          .on("drag", function(d, i) {
            d = (i ? to : from) + 90;
            var start = [-r * Math.cos(d * radians), -r * Math.sin(d * radians)],
                m = [d3.event.x, d3.event.y],
                delta = ~~(Math.atan2(cross2(start, m), dot2(start, m)) / radians);
            d = Math.max(-90, Math.min(90, d + delta - 90)); // remove this for 360°
            delta = to - from;
            if (i) {
              to = d;
              if (delta > 360) from += delta - 360;
              else if (delta < 0) from = to;
            } else {
              from = d;
              if (delta > 360) to += 360 - delta;
              else if (delta < 0) to = from;
            }
            update2();
          })
          .on("dragend", generate));
    drag.attr("transform", function(d) { return "rotate(" + (d + 90) + ")translate(-" + r + ")"; });
    layout2.rotate(function() {
      return scale(~~(Math.random() * count));
    });
    d3.select("#angle-count").property("value", count);
    d3.select("#angle-from").property("value", from);
    d3.select("#angle-to").property("value", to);
  }

  function cross2(a, b) { return a[0] * b[1] - a[1] * b[0]; }
  function dot2(a, b) { return a[0] * b[0] + a[1] * b[1]; }
})();
