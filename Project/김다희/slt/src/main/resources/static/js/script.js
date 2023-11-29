// campland-N1 [ZmLOr0E4gg]
(function() {
  $(function() {
    $(".signLang").each(function () {
      const $block = $(this);
      // Mobile Top
      $block.find(".btn-momenu").on("click", function () {
        $block.addClass("block-active");
      });
      $block.find(".btn-moclose").on("click", function () {
        $block.removeClass("block-active");
      });
      // Mobile Gnb
      $block.find(".header-gnbitem").each(function () {
        const $this = $(this);
        const $thislink = $this.find(".header-gnblink");
        $thislink.on("click", function () {
          if (!$(this).parent().hasClass("item-active")) {
            $(".header-gnbitem").removeClass("item-active");
          }
          $(this).parents(".header-gnbitem").toggleClass("item-active");
        });
      });
      // Header Mobile 1Depth Click
      if (window.innerWidth <= 768) {
        $block.find(".header-gnbitem").each(function () {
          const $gnblink = $(this).find(".header-gnblink");
          const $sublist = $(this).find(".header-sublist");
          if ($sublist.length) {
            $gnblink.attr("href", "javascript:void(0);");
          }
        });
      }
      
      // Full Gnb
      $block.find(".btn-allmenu").on("click", function () {
        console.log("클릭 이벤트 발생 - 메뉴 열기");
        $block.find(".header-fullmenu").addClass("fullmenu-active");
      });
      $block.find(".fullmenu-close").on("click", function () {
        console.log("클릭 이벤트 발생 - 메뉴 닫기");
        $block.find(".header-fullmenu").removeClass("fullmenu-active");
      });
      // Full Gnb DecoLine
      $block.find(".fullmenu-gnbitem").each(function () {
        const $this = $(this);
        $this.on("mouseover", function () {
          if (window.innerWidth > 768) {
            $this.find(".fullmenu-gnblink").addClass("on");
          }
        });
        $this.on("mouseout", function () {
          if (window.innerWidth > 768) {
            $this.find(".fullmenu-gnblink").removeClass("on");
          }
        });
      });
    });
  });
});
