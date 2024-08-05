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
function submitSerch() {

    // フォームの各フィールドの値を取得
    var item = $('#item_select').val();
    var date_column = $('#date_column').val();
    var searchForm = {
        item: item,
        date_column: date_column,
    }
    // CSRFトークンの取得
    var csrfToken = $('meta[name="_csrf"]').attr('content');
    var csrfHeader = $('meta[name="_csrf_header"]').attr('content');

    // AJAXリクエストの送信
    $.ajax({
        url: '/usefulltools/RecodeSearchAjax',// リクエストを送信する先のURLを指定します。この場合、'/usefulltools/RecodeAddAjax'にPOSTリクエストが送信される。
        type: 'POST',
        contentType: 'application/json',//送信するデータの形式を指定します。この場合、JSON形式のデータを送信するために'application/json'が指定される。
        data: JSON.stringify(searchForm),//送信するデータを指定します。formDataオブジェクトをJSON文字列に変換しています。これにより、サーバーにJSON形式のデータが送信される。
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeader, csrfToken);//CSRFトークンを認証の処理をしないと403 Forbiddenエラーが出る
        },
        success: function (response) {
            displayData(response);
        },
        error: function (error) {
            console.error("エラーが発生しました: " + error);
        }
    });

}

function submitAll(){
    recodeList()
}

function submitForm() {//Ajaxで非同期通信を使用してDBへ登録する

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
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeader, csrfToken);//CSRFトークンを認証の処理をしないと403 Forbiddenエラーが出る
        },
        success: function (response) {
            // データ登録成功後に一覧を更新
            displayData(response);
            AnalyticsData();
        },
        error: function (error) {
            console.error("エラーが発生しました: " + error);
        }
    });
}

function recodeList() {//一覧データをDBから非同期通信で拾ってくる
    $.ajax({
        url: '/usefulltools/RecodeDataAjax',
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            displayData(data);
        },
        error: function (error) {
            console.error(error);
        }
    });
}

function displayData(data) {
    var tableBody = $('#data-table tbody');
    tableBody.empty();

    data.forEach(function (item) {
        var row = '<tr>';
        row += '<td>' + item.date_column + '</td>';
        row += '<td>' + item.item + '</td>';
        row += '<td>' + item.event + '</td>';
        row += '<td>' + item.set_column + "set" + '</td>';
        row += '<td>' + item.weight + "kg" + '</td>';
        row += '<td>' + item.rep + "rep" + '</td>';
        row += '<td><button class="btn btn-primary delete-btn" data-id="' + item.save_id + '">削除</button></td>';
        row += '</tr>';
        tableBody.append(row);
    });
}
    function displayData(response) {
        var tableBody = $('#data-table tbody');
        tableBody.empty();
    
        response.forEach(function (item) {
            var row = '<tr>';
            row += '<td>' + item.date_column + '</td>';
            row += '<td>' + item.item + '</td>';
            row += '<td>' + item.event + '</td>';
            row += '<td>' + item.set_column + "set" + '</td>';
            row += '<td>' + item.weight + "kg" + '</td>';
            row += '<td>' + item.rep + "rep" + '</td>';
            row += '<td><button class="btn btn-primary delete-btn" data-id="' + item.save_id + '">削除</button></td>';
            row += '</tr>';
            tableBody.append(row);
        });

    $('.delete-btn').click(function () {
        var id = $(this).data('id');
        deleteRecord(id);
    });
}

function deleteRecord(id) {
    var csrfToken = $('meta[name="_csrf"]').attr('content');
    var csrfHeader = $('meta[name="_csrf_header"]').attr('content');

    $.ajax({
        url: '/usefulltools/RecodeDelete',
        type: 'POST',
        data: { id: id },
        beforeSend: function (xhr) {
            xhr.setRequestHeader(csrfHeader, csrfToken);
        },
        success: function () {
            // データ削除成功後に一覧を更新
            recodeList();
            AnalyticsData();
        },
        error: function (xhr, status, error) {
            console.error(error);
        }
    });
}

$(document).ready(function () {
    recodeList();
});

function AnalyticsData() {
    $.ajax({
        url: '/usefulltools/AnalyticsDataAjax',
        type: 'GET',
        dataType: 'json',
        success: function (responseData) {
            displayAnalyticsData(responseData);
        },
        error: function (error) {
            console.error(error);
        }
    });
}

function displayAnalyticsData(responseData) {
    var totalWeight = responseData.totalWeight;
    var totalCount = responseData.totalCount;
    var dayFromDay = responseData.dayfromday;

    $('#totalWeight h1').text(totalWeight + "kg");
    $('#totalCount h1').text(totalCount + "set");
    $('#dayfromday h1').text(dayFromDay + "日");
}

$(document).ready(function () {
    AnalyticsData();
});

