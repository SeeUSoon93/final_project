/* ========== */
/* Temha */
(function () {
  // Body
  document.body.addEventListener("click", () => {
    document
      .querySelectorAll(".dropset")
      .forEach((element) => element.classList.remove("active"));
    document
      .querySelectorAll(".selectset")
      .forEach((element) => element.classList.remove("active"));
  });

  // Dropset
  const dropsetToggle = document.querySelectorAll(".dropset-toggle");
  dropsetToggle.forEach((buttonElement) => {
    const clickEventHandler = (event) => {
      event.stopPropagation();
      const button = event.target.closest(".dropset-toggle");
      const buttonParent = button.closest(".dropset");
      buttonParent.classList.toggle("active");
    };
    buttonElement.removeEventListener("click", clickEventHandler);
    buttonElement.addEventListener("click", clickEventHandler);
  });
  
  /*
  // Toast
  const toastClose = document.querySelectorAll(".toastset-close");
  toastClose.forEach((buttonElement) => {
    const clickEventHandler = (event) => {
      const button = event.target.closest(".toastset-close");
      const buttonGrandParent = button.closest(".toastset");
      buttonGrandParent.remove();
    };
    buttonElement.removeEventListener("click", clickEventHandler);
    buttonElement.addEventListener("click", clickEventHandler);
  });
  */
  // Videoset
  const videosetButtons = document.querySelectorAll(".videoset-play");
  videosetButtons.forEach((buttonElement) => {
    const clickEventHandler = (countevent) => {
      const buttonElement = event.target
        .closest(".videoset")
        .querySelector(".videoset-video");
      const buttonGrandParent = event.target.closest(".videoset");
      buttonElement.play();
      buttonGrandParent.classList.add("active");
      buttonElement.addEventListener("click", () => {
        buttonElement.pause();
        buttonGrandParent.classList.remove("active");
      });
    };
    buttonElement.removeEventListener("click", clickEventHandler);
    buttonElement.addEventListener("click", clickEventHandler);
  });

  // Checkset
  const checksetElements = document.querySelectorAll(".checkset");
  checksetElements.forEach((checksetElement) => {
    const checksetText = checksetElement.querySelector(".checkset-text");
    const checkboxInput = checksetElement.querySelector(".checkset-input");
    checksetText.addEventListener("click", () => {
      if (!checkboxInput.disabled) {
        checkboxInput.checked = !checkboxInput.checked;
      }
    });
  });

  // Radioset
  const radiosetElements = document.querySelectorAll(".radioset");
  radiosetElements.forEach((radiosetElement) => {
    const radiosetText = radiosetElement.querySelector(".radioset-text");
    const radioboxInput = radiosetElement.querySelector(".radioset-input");
    radiosetText.addEventListener("click", () => {
      if (!radioboxInput.disabled) {
        radioboxInput.checked = !radioboxInput.checked;
      }
    });
  });

  // Function
  function getSiblings(parent, element) {
    return [...parent.children].filter((item) => item !== element);
  }
  function getIndex(element) {
    return [...element.parentNode.children].indexOf(element);
  }
})();
