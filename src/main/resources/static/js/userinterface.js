window.onload = function () {
    const sideMenu = document.querySelector('aside');
    const menuBtn = document.getElementById('menu-btn');
    const closeBtn = document.getElementById('close-btn');

    var currentDateTimeNowOutput = document.getElementById('currentDateTimeNow');



    function updateTime() {
        var now = new Date();
        var nowhour = now.getHours();
        var nowminutes = now.getMinutes();
        var nowseconds = now.getSeconds();
        var currentDateTimeNow = nowhour + ':' + nowminutes + ':' + nowseconds;
        currentDateTimeNowOutput.textContent = currentDateTimeNow;
    }
    updateTime();
    setInterval(updateTime, 1000);

    menuBtn.addEventListener('click', () => {
        sideMenu.style.display = 'block';
    });

    closeBtn.addEventListener('click', () => {
        sideMenu.style.display = 'none';
    });



    // ウィンドウのリサイズを監視し、必要に応じてメニューボタンを表示する
    window.addEventListener('resize', () => {
        if (window.innerWidth > 800) {
            // ウィンドウの幅が800pxより大きい場合、メニューボタンを表示
            sideMenu.style.display = 'block';
        } else {
            // ウィンドウの幅が800px以下の場合、メニューボタンを非表示
            sideMenu.style.display = 'none';
        }
    });

}