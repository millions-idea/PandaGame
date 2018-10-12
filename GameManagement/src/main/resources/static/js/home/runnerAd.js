var c = "mui-slider",
    d = "mui-slider-group",
    e = "mui-slider-loop",
    f = "mui-action-previous",
    g = "mui-action-next",
    h = "mui-slider-item",
    i = "mui-active",
    j = "." + h,
    k = ".mui-slider-progress-bar",
    l = a.Slider = a.Scroll.extend({
        init: function(b, c) {
            this._super(b, a.extend(!0, {
                fingers: 1,
                interval: 0,
                scrollY: !1,
                scrollX: !0,
                indicators: !1,
                scrollTime: 1e3,
                startX: !1,
                slideTime: 0,
                snap: j
            }, c)), this.options.startX
        },
        _init: function() {
            this._reInit(), this.scroller && (this.scrollerStyle = this.scroller.style, this.progressBar = this.wrapper.querySelector(k), this.progressBar && (this.progressBarWidth = this.progressBar.offsetWidth, this.progressBarStyle = this.progressBar.style), this._super(), this._initTimer())
        },
        _triggerSlide: function() {
            var b = this;
            b.isInTransition = !1;
            b.currentPage;
            b.slideNumber = b._fixedSlideNumber(), b.loop && (0 === b.slideNumber ? b.setTranslate(b.pages[1][0].x, 0) : b.slideNumber === b.itemLength - 3 && b.setTranslate(b.pages[b.itemLength - 2][0].x, 0)), b.lastSlideNumber != b.slideNumber && (b.lastSlideNumber = b.slideNumber, b.lastPage = b.currentPage, a.trigger(b.wrapper, "slide", {
                slideNumber: b.slideNumber
            })), b._initTimer()
        },
        _handleSlide: function(b) {
            var c = this;
            if (b.target === c.wrapper) {
                var d = b.detail;
                d.slideNumber = d.slideNumber || 0;
                for (var e = c.scroller.querySelectorAll(j), f = [], g = 0, h = e.length; h > g; g++) {
                    var k = e[g];
                    k.parentNode === c.scroller && f.push(k)
                }
                var l = d.slideNumber;
                if (c.loop && (l += 1), !c.wrapper.classList.contains("mui-segmented-control")) for (var g = 0, h = f.length; h > g; g++) {
                    var k = f[g];
                    k.parentNode === c.scroller && (g === l ? k.classList.add(i) : k.classList.remove(i))
                }
                var m = c.wrapper.querySelector(".mui-slider-indicator");
                if (m) {
                    m.getAttribute("data-scroll") && a(m).scroll().gotoPage(d.slideNumber);
                    var n = m.querySelectorAll(".mui-indicator");
                    if (n.length > 0) for (var g = 0, h = n.length; h > g; g++) n[g].classList[g === d.slideNumber ? "add" : "remove"](i);
                    else {
                        var o = m.querySelector(".mui-number span");
                        if (o) o.innerText = d.slideNumber + 1;
                        else for (var p = m.querySelectorAll(".mui-control-item"), g = 0, h = p.length; h > g; g++) p[g].classList[g === d.slideNumber ? "add" : "remove"](i)
                    }
                }
                b.stopPropagation()
            }
        },
        _handleTabShow: function(a) {
            var b = this;
            b.gotoItem(a.detail.tabNumber || 0, b.options.slideTime)
        },
        _handleIndicatorTap: function(a) {
            var b = this,
                c = a.target;
            (c.classList.contains(f) || c.classList.contains(g)) && (b[c.classList.contains(f) ? "prevItem" : "nextItem"](), a.stopPropagation())
        },
        _initEvent: function(b) {
            var c = this;
            c._super(b);
            var d = b ? "removeEventListener" : "addEventListener";
            c.wrapper[d]("slide", this), c.wrapper[d](a.eventName("shown", "tab"), this)
        },
        handleEvent: function(b) {
            switch (this._super(b), b.type) {
            case "slide":
                this._handleSlide(b);
                break;
            case a.eventName("shown", "tab"):
                ~this.snaps.indexOf(b.target) && this._handleTabShow(b)
            }
        },
        _scrollend: function(a) {
            this._super(a), this._triggerSlide(a)
        },
        _drag: function(a) {
            this._super(a);
            var c = a.detail.direction;
            if ("left" === c || "right" === c) {
                var d = this.wrapper.getAttribute("data-slidershowTimer");
                d && b.clearTimeout(d), a.stopPropagation()
            }
        },
        _initTimer: function() {
            var a = this,
                c = a.wrapper,
                d = a.options.interval,
                e = c.getAttribute("data-slidershowTimer");
            e && b.clearTimeout(e), d && (e = b.setTimeout(function() {
                c && ((c.offsetWidth || c.offsetHeight) && a.nextItem(!0), a._initTimer())
            }, d), c.setAttribute("data-slidershowTimer", e))
        },
        _fixedSlideNumber: function(a) {
            a = a || this.currentPage;
            var b = a.pageX;
            return this.loop && (b = 0 === a.pageX ? this.itemLength - 3 : a.pageX === this.itemLength - 1 ? 0 : a.pageX - 1), b
        },
        _reLayout: function() {
            this.hasHorizontalScroll = !0, this.loop = this.scroller.classList.contains(e), this._super()
        },
        _getScroll: function() {
            var b = a.parseTranslateMatrix(a.getStyles(this.scroller, "webkitTransform"));
            return b ? b.x : 0
        },
        _transitionEnd: function(b) {
            b.target === this.scroller && this.isInTransition && (this._transitionTime(), this.isInTransition = !1, a.trigger(this.wrapper, "scrollend", this))
        },
        _flick: function(a) {
            if (this.moved) {
                var b = a.detail,
                    c = b.direction;
                this._clearRequestAnimationFrame(), this.isInTransition = !0, "flick" === a.type ? (b.deltaTime < 200 && (this.x = this._getPage(this.slideNumber + ("right" === c ? -1 : 1), !0).x), this.resetPosition(this.options.bounceTime)) : "dragend" !== a.type || b.flick || this.resetPosition(this.options.bounceTime), a.stopPropagation()
            }
        },
        _initSnap: function() {
            if (this.scrollerWidth = this.itemLength * this.scrollerWidth, this.maxScrollX = Math.min(this.wrapperWidth - this.scrollerWidth, 0), this._super(), this.currentPage.x) this.slideNumber = this._fixedSlideNumber(), this.lastSlideNumber = "undefined" == typeof this.lastSlideNumber ? this.slideNumber : this.lastSlideNumber;
            else {
                var a = this.pages[this.loop ? 1 : 0];
                if (a = a || this.pages[0], !a) return;
                this.currentPage = a[0], this.slideNumber = 0, this.lastSlideNumber = "undefined" == typeof this.lastSlideNumber ? 0 : this.lastSlideNumber
            }
            this.options.startX = this.currentPage.x || 0
        },
        _getSnapX: function(a) {
            return Math.max(-a, this.maxScrollX)
        },
        _getPage: function(a, b) {
            return this.loop ? a > this.itemLength - (b ? 2 : 3) ? (a = 1, time = 0) : (b ? -1 : 0) > a ? (a = this.itemLength - 2, time = 0) : a += 1 : (b || (a > this.itemLength - 1 ? (a = 0, time = 0) : 0 > a && (a = this.itemLength - 1, time = 0)), a = Math.min(Math.max(0, a), this.itemLength - 1)), this.pages[a][0]
        },
        _gotoItem: function(b, c) {
            this.currentPage = this._getPage(b, !0), this.scrollTo(this.currentPage.x, 0, c, this.options.scrollEasing), 0 === c && a.trigger(this.wrapper, "scrollend", this)
        },
        setTranslate: function(a, b) {
            this._super(a, b);
            var c = this.progressBar;
            c && (this.progressBarStyle.webkitTransform = this._getTranslateStr(-a * (this.progressBarWidth / this.wrapperWidth), 0))
        },
        resetPosition: function(a) {
            return a = a || 0, this.x > 0 ? this.x = 0 : this.x < this.maxScrollX && (this.x = this.maxScrollX), this.currentPage = this._nearestSnap(this.x), this.scrollTo(this.currentPage.x, 0, a, this.options.scrollEasing), !0
        },
        gotoItem: function(a, b) {
            this._gotoItem(a, "undefined" == typeof b ? this.options.scrollTime : b)
        },
        nextItem: function() {
            this._gotoItem(this.slideNumber + 1, this.options.scrollTime)
        },
        prevItem: function() {
            this._gotoItem(this.slideNumber - 1, this.options.scrollTime)
        },
        getSlideNumber: function() {
            return this.slideNumber || 0
        },
        _reInit: function() {
            for (var a = this.wrapper.querySelectorAll("." + d), b = 0, c = a.length; c > b; b++) if (a[b].parentNode === this.wrapper) {
                this.scroller = a[b];
                break
            }
            this.scrollerStyle = this.scroller && this.scroller.style, this.progressBar && (this.progressBarWidth = this.progressBar.offsetWidth, this.progressBarStyle = this.progressBar.style)
        },
        refresh: function(b) {
            b ? (a.extend(this.options, b), this._super(), this._initTimer()) : this._super()
        },
        destroy: function() {
            this._initEvent(!0), delete a.data[this.wrapper.getAttribute("data-slider")], this.wrapper.setAttribute("data-slider", "")
        }
    });
a.fn.slider = function(b) {
    var d = null;
    return this.each(function() {
        var e = this;
        if (this.classList.contains(c) || (e = this.querySelector("." + c)), e && e.querySelector(j)) {
            var f = e.getAttribute("data-slider");
            f ? (d = a.data[f], d && b && d.refresh(b)) : (f = ++a.uuid, a.data[f] = d = new l(e, b), e.setAttribute("data-slider", f))
        }
    }), d
}, a.ready(function() {
    a(".mui-slider").slider(), a(".mui-scroll-wrapper.mui-slider-indicator.mui-segmented-control").scroll({
        scrollY: !1,
        scrollX: !0,
        indicators: !1,
        snap: ".mui-control-item"
    })
})