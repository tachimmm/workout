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


function submitForm() { //Ajaxで非同期通信を使用してDBへ登録する

    // フォームの各フィールドの値を取得
    var date = $('#date').val();
    var item = $('#item').val();
    var event = $('#event').val();
    var set = $('#setCount').val();
    var weight = $('#weight').val();
    var rep = $('#repCount').val();

    // 各フィールドのバリデーションを行う
    if (!date || !item || !event || isNaN(set) || isNaN(weight) || isNaN(rep)) {
        // 必須フィールドが空白であるか、数値フィールドに数値以外の値が入力されている場合
        alert('すべて入力してください');
        return; // フォームの送信を中止
    }

    // フォームデータの作成
    var formData = { //プロパティ名はSpring側のRecodeFormクラスのフィールドと一致している必要がある
        date: date,
        item: item,
        event: event,
        set: parseInt(set),
        weight: parseFloat(weight),
        rep: parseInt(rep)
    };

    // CSRFトークンの取得
    var csrfToken = $('meta[name="_csrf"]').attr('content');
    var csrfHeader = $('meta[name="_csrf_header"]').attr('content');

    // AJAXリクエストの送信
    $.ajax({
        url: '/usefulltools/RecodeAddAjax',// リクエストを送信する先のURLを指定します。この場合、'/usefulltools/RecodeAddAjax'にPOSTリクエストが送信される。
        type: 'POST',
        contentType: 'application/json',//送信するデータの形式を指定します。この場合、JSON形式のデータを送信するために'application/json'が指定される。
        data: JSON.stringify(formData),//送信するデータを指定します。formDataオブジェクトをJSON文字列に変換しています。これにより、サーバーにJSON形式のデータが送信される。
        beforeSend: function (xhr) {//CSRFトークンを認証の処理をしないと403 Forbiddenエラーが出る
            xhr.setRequestHeader(csrfHeader, csrfToken);
        },
        success: function (response) {
            window.location.href = "/usefulltools/content-work-out-recorde";
        },
        error: function (xhr, status, error) {
            console.error("エラーが発生しました: " + error);
        }
    });
}
