// HTML要素のロードが完了した後に実行
window.onload = function () {
    // 入力フィールドの要素を取得
    var heightInput = document.getElementById("heightInput");
    var weightInput = document.getElementById("weightInput");
    var useweightInput = document.getElementById("useweightInput");
    var pfcWeightInput = document.getElementById("pfcWeightInput");
    var pfcHeightInput = document.getElementById("pfcHeightInput");
    var pfcAgeInput = document.getElementById("pfcAgeInput");
    var activelevelInput = document.getElementById("activelevelInput");
    var purposeInput = document.getElementById("purposeInput");
    var pfcSexInput = document.getElementById("pfcSexInput");

    // 表示する要素を取得
    var bmiOutput = document.getElementById("bmiOutput");
    var rmOutput = document.getElementById("rmOutput");
    var repInput = document.getElementById("repInput");
    var basalMetabolismOutput = document.getElementById("basalMetabolismOutput");
    var totalCalOutput = document.getElementById("totalCalOutput");
    var proteinOutput = document.getElementById("proteinOutput");
    var fatOutput = document.getElementById("fatOutput");
    var carbohydrateOutput = document.getElementById("carbohydrateOutput");

    // 入力フィールドにinputイベントリスナーを追加
    heightInput.addEventListener("input", updateBMI);
    weightInput.addEventListener("input", updateBMI);
    useweightInput.addEventListener("input", updateRM);
    pfcWeightInput.addEventListener("input", updatePFC);
    pfcHeightInput.addEventListener("input", updatePFC);
    pfcAgeInput.addEventListener("input", updatePFC);
    activelevelInput.addEventListener("input", updatePFC);
    purposeInput.addEventListener("input", updatePFC);
    pfcSexInput.addEventListener("input", updatePFC);
    repInput.addEventListener("input", updateRM);

    // BMIを更新する関数
    function updateBMI() {
        // 入力された身長と体重を取得
        var height = parseFloat(heightInput.value);
        var weight = parseFloat(weightInput.value);

        // 身長と体重が有効な数値であるかをチェック
        if (!isNaN(height) && !isNaN(weight)) {
            // BMIを計算
            var bmi = calculateBMI(weight, height);
            // BMIを表示
            bmiOutput.textContent = bmi.toFixed(1);
        } else {
            // 身長と体重のどちらかが無効な場合は、BMIを0.0として表示
            bmiOutput.textContent = "0.0";
        }
    }

    // BMIを計算する関数
    function calculateBMI(weight, height) {
        // 身長をメートルに変換
        var heightInMeter = height / 100.0;
        // BMIを計算
        var bmi = Math.floor(weight / (heightInMeter * heightInMeter) * 10) / 10;
        return bmi;
    }

    // RMを更新する関数
    function updateRM() {
        //入力された最大重量とREP数を取得
        var useweight = parseFloat(useweightInput.value);
        var rep = parseFloat(repInput.value);

        //有効な数字であるかチェック
        if (!isNaN(useweight) && !isNaN(rep)) {
            //RMを計算
            var rm = calculateRM(useweight, rep);
            //RMを表示
            rmOutput.textContent = rm.toFixed(1);
        } else {
            // 使用重量とREP数のどちらかが無効な場合は、RMを0.0として表示
            rmOutput.textContent = "0.0";
        }
    }

    //RMを計算する関数
    function calculateRM(useweight, rep) {
        var rm = useweight * (1 + (rep / 40));
        return rm;
    }

    // PFCを更新する関数
    function updatePFC() {
        //入力された値を取得
        var pfcWegight = parseFloat(pfcWeightInput.value);
        var pfcHeight = parseFloat(pfcHeightInput.value);
        var pfcage = parseFloat(pfcAgeInput.value);
        var activelevel = parseFloat(activelevelInput.value);
        var purpose = parseFloat(purposeInput.value);
        var sex = pfcSexInput.value;

        if (!isNaN(pfcWegight) && !isNaN(pfcHeight) && !isNaN(pfcage) && !isNaN(activelevel) && !isNaN(purpose)) {
            //PFCを計算
            var basalMetabolism = calculatebasalMetabolismOutput(pfcWegight, pfcHeight, pfcage, activelevel, sex);
            var totalCal = calculatetotalCalOutput(pfcWegight, pfcHeight, pfcage, activelevel, sex, purpose);
            var protein = calculateproteinOutput(pfcWegight);
            var fat = calculatefat(pfcWegight, pfcHeight, pfcage, activelevel, sex, purpose);
            var carbohydrate = calculatecarbohydrate(pfcWegight, pfcHeight, pfcage, activelevel, sex, purpose);
            //結果を表示
            basalMetabolismOutput.textContent = basalMetabolism.toFixed(1);
            totalCalOutput.textContent = totalCal.toFixed(1);
            proteinOutput.textContent = protein.toFixed(1);
            fatOutput.textContent = fat.toFixed(1);
            carbohydrateOutput.textContent = carbohydrate.toFixed(1);
        } else {
            // 入力が無効な場合は、すべての結果を0.0として表示
            basalMetabolismOutput.textContent = "0.0";
            totalCalOutput.textContent = "0.0";
            proteinOutput.textContent = "0.0";
            fatOutput.textContent = "0.0";
            carbohydrateOutput.textContent = "0.0";
        }
    }

    // 基礎代謝を計算する関数
    function calculatebasalMetabolismOutput(pfcWegight, pfcHeight, pfcage, sex) {
        var bmr = 0.0;

        if (sex === "男") {
            bmr = Math.floor((66.5 + (pfcWegight * 13.8) + (pfcHeight * 5.0) - (pfcage * 6.8)) );
        } else {
            bmr = Math.floor((665 + pfcWegight * 9.6 + pfcHeight * 1.9 - pfcage * 4.7));
        }
        return bmr; // 計算結果を返す
    }

    // 総カロリーを計算する関数
    function calculatetotalCalOutput(pfcWegight, pfcHeight, pfcage, activelevel, sex, purpose) {
        var totalCal = 0.0;
        totalCal = Math.floor((calculatebasalMetabolismOutput(pfcWegight, pfcHeight, pfcage, activelevel, sex) * activelevel) + purpose);
        return totalCal;
    }

    // タンパク質を計算する関数
    function calculateproteinOutput(pfcWegight) {
        var protein = 0.0;
        protein = Math.floor(pfcWegight * 1.6);
        return protein;
    }

    // 脂肪を計算する関数
    function calculatefat(pfcWegight, pfcHeight, pfcage, activelevel, sex, purpose) {
        var fat = 0.0;
        fat = Math.floor((0.3 * calculatetotalCalOutput(pfcWegight, pfcHeight, pfcage, activelevel, sex, purpose)) / 9);
        return fat;
    }

    // 炭水化物を計算する関数
    function calculatecarbohydrate(pfcWegight, pfcHeight, pfcage, activelevel, sex, purpose) {
        var carbohydrate = 0.0;
        carbohydrate = Math.floor((0.7 * calculatetotalCalOutput(pfcWegight, pfcHeight, pfcage, activelevel, sex, purpose)) / 4);
        return carbohydrate;
    }

const sideMenu = document.querySelector('aside');
const menuBtn = document.getElementById('menu-btn');
const closeBtn = document.getElementById('close-btn');

var currentDateTimeNowOutput = document.getElementById('currentDateTimeNow');



function updateTime(){
    var now = new Date();
    var nowhour = now.getHours();
    var nowminutes = now.getMinutes();
    var nowseconds = now.getSeconds();
    var currentDateTimeNow = nowhour + ':' + nowminutes + ':' + nowseconds ; 
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
};
