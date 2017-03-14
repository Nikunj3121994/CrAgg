
// JQuery/Materialize init functions

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

$(document).ready(function(){
    $('ul.tabs').tabs();
});

$(document).ready(function(){
    // the "href" attribute of .modal-trigger must specify the modal ID that wants to be triggered
    $('.modal').modal();
});

// Utility functions for interfacing with the rest api

function starResult(dbsource, dbid) {
    $.ajax({
        url: '/api/starResult',
        type: 'PUT',
        data: JSON.stringify({
            db: dbsource,
            id: dbid
        }),
        contentType: 'application/json'
    });
}

function unstarResult(dbsource, dbid) {
    $.ajax({
        url: '/api/unStarResult',
        type: 'PUT',
        data: JSON.stringify({
            db: dbsource,
            id: dbid
        }),
        contentType: 'application/json'
    });
}

function toggleResultStarred(buttonsource, dbsource, dbid) {

    var starIcon = "star";
    var destarIcon = "star_border";

    var starText = "star";
    var notStarredText = "unstar";

    var btnicon = buttonsource.getElementsByTagName("i")[0].innerHTML;


    if(btnicon == destarIcon){

        starResult(dbsource, dbid);

        buttonsource.getElementsByTagName("i")[0].innerHTML = starIcon;
        buttonsource.getElementsByTagName("span")[0].innerHTML = notStarredText;
    } else {

        unstarResult(dbsource, dbid);

        buttonsource.getElementsByTagName("i")[0].innerHTML = destarIcon;
        buttonsource.getElementsByTagName("span")[0].innerHTML = starText;
    }

}

function downloadResult(buttonsource, dbsource, dbid) {
    $.ajax({
        url: '/api/downloadResult/' + dbsource + '/' + dbid + '/',
        type: 'GET',
        success: function(result) {

        }
    });
}
