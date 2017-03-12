(function($){
  $(function(){

    $('.button-collapse').sideNav();

  }); // end of document ready
})(jQuery); // end of jQuery name space

$(document).ready(function() {
    $('select').material_select();
});
  
$(document).ready(function(){
    $('.parallax').parallax();
});

$(document).ready(function(){
    $('.collapsible').collapsible();
});

function toggleResultSelected(buttonsource, dbsource, dbid) {
    $.ajax({
        url: '/api/selectResult',
        type: 'PUT',
        data: JSON.stringify({
            db: dbsource,
            id: dbid
        }),
        contentType: 'application/json',
        success: function(result) {
            // alternate the buttons class here, state should be initially set on page load and checking the
            // session cache
            var selectIcon = "check_box";
            var deselectIcon = "check_box_outline_blank";

            var selectText = "select";
            var notSelectedText = "deselect";

            var btnicon = buttonsource.getElementsByTagName("i")[0].innerHTML;

            if(btnicon == deselectIcon){
                buttonsource.getElementsByTagName("i")[0].innerHTML = selectIcon;
                buttonsource.getElementsByTagName("span")[0].innerHTML = notSelectedText;
            } else {
                buttonsource.getElementsByTagName("i")[0].innerHTML = deselectIcon;
                buttonsource.getElementsByTagName("span")[0].innerHTML = selectText;
            }

        }
    });
}

function toggleResultStarred(buttonsource, dbsource, dbid) {
    $.ajax({
        url: '/api/starResult',
        type: 'PUT',
        data: JSON.stringify({
            db: dbsource,
            id: dbid
        }),
        contentType: 'application/json',
        success: function(result) {
            // alternate the buttons class here, state should be initially set on page load and checking the
            // session cache
            var starIcon = "star";
            var destarIcon = "star_border";

            var starText = "star";
            var notStarredText = "unstar";

            var btnicon = buttonsource.getElementsByTagName("i")[0].innerHTML;

            if(btnicon == destarIcon){
                buttonsource.getElementsByTagName("i")[0].innerHTML = starIcon;
                buttonsource.getElementsByTagName("span")[0].innerHTML = notStarredText;
            } else {
                buttonsource.getElementsByTagName("i")[0].innerHTML = destarIcon;
                buttonsource.getElementsByTagName("span")[0].innerHTML = starText;
            }

        }
    });
}

function downloadResult(buttonsource, dbsource, dbid) {
    $.ajax({
        url: '/api/downloadResult/' + dbsource + '/' + dbid + '/',
        type: 'GET',
        success: function(result) {

        }
    });
}
