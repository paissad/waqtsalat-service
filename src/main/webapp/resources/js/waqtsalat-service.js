
$(document).ready(function(){
	$(".fieldAsterisk_2").after("<span class='fieldAsterisk exposant'></span>");
});

$('textarea.maxLength_500').keyup(function() {
    var $textarea = $(this);
    var max = 500;
    if ($textarea.val().length > max) {
        $textarea.val($textarea.val().substr(0, max));
    }
});

