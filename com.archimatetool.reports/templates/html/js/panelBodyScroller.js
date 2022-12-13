document.addEventListener('DOMContentLoaded', function () {
  const ele = document.getElementsByClassName('root-panel-body')[0];

  let pos = { top: 0, left: 0, x: 0, y: 0 };
  let isBlockLink = false;

  const tags = ele.getElementsByTagName('area');
  for (var i = 0; i < tags.length; i++) {
    tags[i].addEventListener('click', function (e) {
      if(isBlockLink) e.preventDefault();
    });
  }

  const diagram = ele.getElementsByClassName('diagram')[0];
  if(diagram) {
    diagram.addEventListener('mousedown', function (e) {
      e.preventDefault();
    });
  }
  
  const mouseDownHandler = function (e) {
    ele.style.cursor = 'grabbing';
    ele.style.userSelect = 'none';

    pos = {
      left: ele.scrollLeft,
      top: ele.scrollTop,
      // Get the current mouse position
      x: e.clientX,
      y: e.clientY,
    };

    document.addEventListener('mousemove', mouseMoveHandler);
    document.addEventListener('mouseup', mouseUpHandler);
  };

  const mouseMoveHandler = function (e) {
    isBlockLink = true;
    // How far the mouse has been moved
    const dx = e.clientX - pos.x;
    const dy = e.clientY - pos.y;

    // Scroll the element
    ele.scrollTop = pos.top - dy;
    ele.scrollLeft = pos.left - dx;
  };

  const mouseUpHandler = function () {
    ele.style.cursor = 'auto';
    ele.style.removeProperty('user-select');

    document.removeEventListener('mousemove', mouseMoveHandler);
    document.removeEventListener('mouseup', mouseUpHandler);

    setTimeout(function ()  {
      isBlockLink = false;
    }, 100)
  };

  // Attach the handler
  ele.addEventListener('mousedown', mouseDownHandler);
});
