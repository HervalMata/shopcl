$(document).ready(function () {
    $("#buttonCancel").on("click", function () {
        window.location = "[[@{/users}]]";
    });
    $("#fileImage").change(function () {
        fileSize = this.files[0].size;
        if (fileSize > 102400) {
            this.setCustomValidity("Você deve escolher uma imagem menor que 1MB!");
            this.reportValidity();
        } else {
            this.setCustomValidity("");
            showImageThumbnail(this);
        }
    });
});
function showImageThumbnail(fileInput) {
    var file = fileInput.files[0];
    var reader = new FileReader();
    reader.onload = function (e) {
        $("#thumbnail").attr("src", e.target.result);
    };
    reader.readAsDataURL(file);
}