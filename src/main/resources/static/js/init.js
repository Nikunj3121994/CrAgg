
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

function downloadAllSelected() {

    var finaljson = [];

    $("input:checkbox").each( function( index, element ){

        if(element.checked){
            var db = element.getAttribute("data-db");
            var id = element.getAttribute("data-id");

            finaljson[index] = {db:db, id:id};

        }
    });

    console.log(finaljson);

    /*$.ajax({
        url: '/api/downloadMany',
        type: 'POST',
        data: JSON.stringify(finaljson),
        contentType: 'application/json'
    });*/

    // thanks mystery man http://stackoverflow.com/questions/16086162/handle-file-download-from-ajax-post
    // Use XMLHttpRequest instead of Jquery $ajax
    var xhttp = new XMLHttpRequest();
    xhttp.onreadystatechange = function() {
        var a;
        if (xhttp.readyState === 4 && xhttp.status === 200) {
            // Trick for making downloadable link
            a = document.createElement('a');
            a.href = window.URL.createObjectURL(xhttp.response);
            // Give filename you wish to download
            a.download = "cifdownload.zip";
            a.style.display = 'none';
            document.body.appendChild(a);
            a.click();
        }
    };
// Post data to URL which handles post request
    xhttp.open("POST", '/api/downloadMany');
    xhttp.setRequestHeader("Content-Type", "application/json");
// You should set responseType as blob for binary responses
    xhttp.responseType = 'blob';
    xhttp.send(JSON.stringify(finaljson));

}
