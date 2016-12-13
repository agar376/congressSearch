localStorage = {};


function toggleMenu() {
    if (document.getElementById("sidemenu").style.width == "" || document.getElementById("sidemenu").style.width == "10%") {
        document.getElementById("sidemenu").style.width = "0px";
        document.getElementById("main-container").style.marginLeft = "0px";
        document.getElementById("main-container").style.width = "100%";
    } else {
        document.getElementById("sidemenu").style.width = "10%";
        document.getElementById("main-container").style.marginLeft = "10%";
        document.getElementById("main-container").style.width = "90%";
    }
}

(function() {
    app = angular.module('myApp', ['angularUtils.directives.dirPagination']);
    var stateSet = new Set();
    var stateArray = [];
    app.controller('legislatorsController', ['$scope', '$http', function($scope, $http) {
        $http({
            url: "congress.php",
            method: "GET",
            dataType: "json",
            params: {
                "database": "0",
                "filter": "state",
            }
        }).success(function(data) {
            if (data.results && data.results.length > 0) {
                $scope.legislatorsStateController = createTable(data, 0, 0);
                for (var i = 0; i < data.results.length; i++) {
                    stateSet.add(data.results[i].state_name);
                }
                stateArray = Array.from(stateSet).sort();
                $scope.stateNames = stateArray;
            }
        }).error(function(data, status, headers, config) {

        });
        $scope.pageChangeHandler = function(num) {
            console.log('meals page changed to ' + num);
        };
        $scope.currentPage = 1;
        $scope.pageSize = 10;
        $scope.currentPageHouse = 1;
        $scope.currentPageSenate = 1;

        // $scope.stateNames = ['Iowa', 'Ohio', 'Utah', 'Idaho', 'Maine', 'Texas', 'Alaska', 'Hawaii', 'Kansas', 'Nevada', 'Oregon', 'Alabama', 'Arizona', 'Florida', 'Georgia', 'Indiana', 'Montana', 'Vermont', 'Wyoming', 'Arkansas', 'Colorado', 'Delaware', 'Illinois', 'Kentucky', 'Maryland', 'Michigan', 'Missouri', 'Nebraska', 'NewYork', 'Oklahoma', 'Virginia', 'Louisiana', 'Minnesota', 'Tennessee', 'Wisconsin', 'California', 'NewJersey', 'NewMexico', 'Washington', 'Connecticut', 'Mississippi', 'NorthDakota', 'Pennsylvania', 'RhodeIsland', 'SouthDakota', 'Massachusetts', 'NewHampshire', 'WestVirginia', 'NorthCarolina', 'SouthCarolina', 'DistrictofColumbia'];

        $http({
            url: "congress.php",
            method: "GET",
            dataType: "json",
            params: {
                "database": "0",
                "filter": "senate",
            }
        }).success(function(data) {
            if (data.results && data.results.length > 0) {
                $scope.legislatorsSenateController = createTable(data, 0, 0);
            }
        }).error(function(data, status, headers, config) {

        });

        $http({
            url: "congress.php",
            method: "GET",
            dataType: "json",
            params: {
                "database": "0",
                "filter": "house",
            }
        }).success(function(data) {
            if (data.results && data.results.length > 0) {
                $scope.legislatorsHouseController = createTable(data, 0, 0);
            }
        }).error(function(data, status, headers, config) {

        });

    }]);

    app.config(['$compileProvider', function($compileProvider) {
        $compileProvider.aHrefSanitizationWhitelist(/^\s*(https?|file|tel|javascript):/);
    }]);


    app.controller('committeesController', ['$scope', '$http', function($scope, $http) {
        $http({
            url: "congress.php",
            method: "GET",
            dataType: "json",
            params: {
                "database": "2",
                "filter": "house",
            }
        }).success(function(data) {
            if (data.results && data.results.length > 0) {
                $scope.committeesHouseController = createTable(data, 2, 0);
            }
        }).error(function(data, status, headers, config) {

        });

        $scope.currentPage = 1;
        $scope.currentPageJoint = 1;
        $scope.currentPageSenate = 1;
        $scope.pageSize = 10;

        $http({
            url: "congress.php",
            method: "GET",
            dataType: "json",
            params: {
                "database": "2",
                "filter": "senate",
            }
        }).success(function(data) {
            if (data.results && data.results.length > 0) {
                $scope.committeesSenateController = createTable(data, 2, 0);
            }
        }).error(function(data, status, headers, config) {

        });

        $http({
            url: "congress.php",
            method: "GET",
            dataType: "json",
            params: {
                "database": "2",
                "filter": "joint",
            }
        }).success(function(data) {
            if (data.results && data.results.length > 0) {
                $scope.committeesJointController = createTable(data, 2, 0);
            }
        }).error(function(data, status, headers, config) {

        });
    }]);

    app.controller('billsController', ['$scope', '$http', function($scope, $http) {
        $http({
            url: "congress.php",
            method: "GET",
            dataType: "json",
            params: {
                "database": "1",
                "filter": "active",
            }
        }).success(function(data) {
            if (data.results && data.results.length > 0) {
                $scope.billsActiveController = createTable(data, 1, 0);
            }
        }).error(function(data, status, headers, config) {

        });

        $scope.currentPage = 1;
        $scope.currentPageNew = 1;
        $scope.pageSize = 10;

        $http({
            url: "congress.php",
            method: "GET",
            dataType: "json",
            params: {
                "database": "1",
                "filter": "new",
            }
        }).success(function(data) {
            if (data.results && data.results.length > 0) {
                $scope.billsNewController = createTable(data, 1, 0);
            }
        }).error(function(data, status, headers, config) {

        });

    }]);

})();


window.onload = function() {

    localStorage = {};

    $('#myCarousel').carousel({
        interval: false,
        pause: true
    });

    showList(0, 'state');
    loadFavData();
}

function loadFavData() {
    for(var i = 0; i < 3; i++) {
        var arr = localStorage[i];
        if(arr) {
            arr = arr.split(",");
            for(var j = 0; j < arr.length; j++) {
                updateFavPanel(i, arr[j]);
            }
        }
    }
}


function showList(database, filter, subfilter) {
    $('.carousel').carousel(0); 
    
    if (database == 0) {
        document.getElementById('legislators').style.display = "block";
        if (document.getElementById('bills'))
            document.getElementById('bills').style.display = "none";
        if (document.getElementById('committees'))
            document.getElementById('committees').style.display = "none";
        if (document.getElementById('favorites'))
            document.getElementById('favorites').style.display = "none";
        $('#title').html('Legislators');
    } else if (database == 1) {
        document.getElementById('legislators').style.display = "none";
        document.getElementById('bills').style.display = "block";
        if (document.getElementById('committees'))
            document.getElementById('committees').style.display = "none";
        if (document.getElementById('favorites'))
            document.getElementById('favorites').style.display = "none";

        $('#title').html('Bills');
    } else if (database == 2) {
        document.getElementById('legislators').style.display = "none";
        document.getElementById('bills').style.display = "none";
        document.getElementById('committees').style.display = "block";
        if (document.getElementById('favorites'))
            document.getElementById('favorites').style.display = "none";

        $('#title').html('Committees');
    } else if (database == 3) {
        document.getElementById('legislators').style.display = "none";
        document.getElementById('bills').style.display = "none";
        document.getElementById('committees').style.display = "none";
        document.getElementById('favorites').style.display = "block";
        $('#title').html('Favorites');
    }
}

function createTable(jsonData, database, favIcon) {
    var leg_arr = [];
    var com_arr = [];
    var bill_arr = [];
    text = "";
    text += "<table class=\"table table-content\" id=\"content\">";

    house = "<img src=\"images/h.png\" alt=\"Image not available\" width=30 height=30/>";
    senate = "<img src=\"images/s.svg\" alt=\"Image not available\" width=30 height=30/>";

    if (database == 0) {
        for (var i = 0; i < jsonData.results.length; i++) {
            var ro = new Object();
            var r = jsonData.results[i];
            var img = "";
            district = "N.A";
            if (r.district) {
                district = "District " + r.district;
            }
            if (r.chamber == "house") {
                img = house;
            } else {
                img = senate;
            }

            ro.party = r.party.toLowerCase().trim();
            ro.chamber = r.chamber.charAt(0).toUpperCase() + r.chamber.slice(1);
            ro.name = r.last_name + ", " + r.first_name;
            ro.district = district;
            ro.state = r.state_name;

            ro.id = r.bioguide_id;

            leg_arr.push(ro);
        }
        return leg_arr;
    } else if (database == 1) {
        for (var i = 0; i < jsonData.results.length; i++) {
            var ro = new Object();
            var r = jsonData.results[i];
            var img = "";
            district = "N.A";
            if (r.district) {
                district = "District " + r.district;
            }
            if (r.chamber == "house") {
                img = house;
            } else {
                img = senate;
            }

            ro.chamber = r.chamber.charAt(0).toUpperCase() + r.chamber.slice(1);
            ro.introduced_on = r.introduced_on;
            ro.title = r.official_title;
            ro.sponsor = r.sponsor.title + ". " + r.sponsor.last_name + ", " + r.sponsor.first_name;

            ro.id = r.bill_id.toUpperCase();
            ro.bill_id = r.bill_id;
            ro.type = r.bill_type.toUpperCase();

            bill_arr.push(ro);
        }
        return bill_arr;
    } else if (database == 2) {
        for (var i = 0; i < jsonData.results.length; i++) {
            var ro = new Object();
            var r = jsonData.results[i];
            var fav = 0;
            var img = "";
            var phone = "N.A";
            var office = "N.A";
            if (r.phone) {
                phone = r.phone;
            }
            if (r.office) {
                office = r.office;
            }
            if (r.chamber == "house") {
                img = house;
            } else {
                img = senate;
            }
            var parent_committee = "";

            if (r.parent_committee_id) {
                parent_committee = r.parent_committee_id;
            }

            if(localStorage[database]) {
                arr = localStorage[database].split(',');
                index = $.inArray(r.committee_id, arr);
                if(index != -1) {
                    fav = 1;
                }
            }

            ro.chamber = r.chamber.charAt(0).toUpperCase() + r.chamber.slice(1);
            ro.contact = phone;
            ro.parent = parent_committee;
            ro.name = r.name;
            ro.office = office;
            ro.id = r.committee_id;
            ro.fav = fav;


            com_arr.push(ro);
        }
        return com_arr;
    } else if (database == 3) {

    } else {
        alert('ERROR!');
        return;
    }

    text += "</table>";

    return text;
}

function viewDetails(database, id) {
    $.ajax({
        url: "congress.php",
        dataType: "json",
        data: {
            database: database,
            id: id
        },
        success: function(data) {
            $('#details_table').html("");
            $('#object_div').html("");
            if (data.results.length > 0) {
                html = createDetailsTable(data, database, id);
                $('#details_table').html(html);
            } else {
                alert(data);
                alert('No data available for this query!');
            }
        }
    });
    next_slide();

}

function createDetailsTable(jsonData, database, id) {

    $("a#favIconHref").attr('href', "javascript:addToFavourite(" + database + ", '" + id + "')");
    $("span.fav").attr('id', database + "_" + id);

    updateFavIcon(database, id);

    text = "";

    house = "<img src=\"images/h.png\" alt=\"Image not available\" width=30 height=30/>";
    senate = "<img src=\"images/s.svg\" alt=\"Image not available\" width=30 height=30/>";

    if (database == 0) {
        r = jsonData.results[0];
        text += "<div style=\"float:left; width:50%;\">";
        text += "<img src=\"http://theunitedstates.io/images/congress/original/" + r.bioguide_id + ".jpg\" style=\"padding-bottom:20px; width:250px; height:300px; float:left\"/>";
        text += "</div>";

        text += "<table class=\"table table-content details\" id=\"content\" style=\"width:48%; float:left;\">";
        text += "<tr>";
        text += "<td class=\"col-md-6\">" + r.title + ". " + r.last_name + ", " + r.first_name + "</td>";
        text += "</tr>";

        text += "<tr>";
        if (r.oc_email) {
            text += "<td class=\"col-md-6 summersky\"><a href=\"mailto:" + r.oc_email + "\">" + r.oc_email + "</a></td>";
        } else {
            text += "<td>No Email Available</td>";
        }
        text += "</tr>";

        text += "<tr>";
        text += "<td class=\"col-md-6\">Chamber: " + r.chamber.charAt(0).toUpperCase() + r.chamber.slice(1) + "</td>";
        text += "</tr>";

        text += "<tr>";
        text += "<td class=\"col-md-6\">Contact: <span class=\"summersky\">" + r.phone + "</span></td>";
        text += "</tr>";

        text += "<tr>";
        party = "";
        if (r.party.toLowerCase().trim() == "r") {
            party = "Republic";
        } else if (r.party.toLowerCase().trim() == "d") {
            party = "Democrat";
        } else {
            party = "Independent";
        }
        text += "<td class=\"col-md-6\">" + "<img src=\"images/" + r.party.toLowerCase().trim() + ".png\" alt=\"Image not available\" width=30 height=30 style=\"margin-right:10px;\">" + party + "</td>";
        text += "</tr>";

        text += "</table>";
        text += "<table class=\"table table-content details\" id=\"content\">";

        var now = new Date();
        var start = new Date(r.term_start);
        var end = new Date(r.term_end);

        text += "<tr>";
        text += "<th class=\"col-md-2 hidden-xs\"> Start Term </th>";
        text += "<td class=\"col-md-10\">" + moment(start).format("MMM DD, YYYY") + "</td>";
        text += "</tr>";

        text += "<tr>";
        text += "<th class=\"col-md-2 hidden-xs\"> End Term </th>";
        text += "<td class=\"col-md-10\">" + moment(end).format("MMM DD, YYYY") + "</td>";
        text += "</tr>";

        text += "<tr>";
        text += "<th class=\"col-md-2 hidden-xs\"> Term </th>";
        text += "<td class=\"col-md-10\">";


        width = 100;
        if (((end - start) > 0) && (now > start)) {
            width = ((now - start) / (end - start)) * 100;
        } else if (now < start) {
            width = 0;
        }
        width = Math.round(width);
        text += "<div class=\"progress hidden-xs\">";
        text += "<div class=\"progress-bar progress-bar-success\" role=\"progressbar\" aria-valuenow=\"" + width + "\"aria-valuemin=\"0\" aria-valuemax=\"100\" style=\"width:" + width + "%\">";
        text += width + "%</div></div>";
        text += "</td>";
        text += "</tr>";

        text += "<tr>";
        text += "<th class=\"col-md-2 hidden-xs\"> Office </th>";
        text += "<td class=\"col-md-10\">" + r.office + "</td>";
        text += "</tr>";

        text += "<tr>";
        text += "<th class=\"col-md-2 hidden-xs hidden-xs\"> State </th>";
        text += "<td class=\"col-md-10\">" + r.state + "</td>";
        text += "</tr>";

        text += "<tr>";
        text += "<th class=\"col-md-2 hidden-xs\"> Fax </th>";
        text += "<td class=\"col-md-10 summersky\">" + r.fax + "</td>";
        text += "</tr>";

        text += "<tr>";
        text += "<th class=\"col-md-2 hidden-xs\"> Birthday </th>";
        text += "<td class=\"col-md-10\">" + moment(new Date(r.birthday)).format("MMM DD, YYYY") + "</td>";
        text += "</tr>";

        text += "<tr>";
        text += "<th class=\"col-md-2 hidden-xs\"> Social Links </th>";
        text += "<td class=\"col-md-10\">";
        if (r.twitter_id) {
            text += "<a href=\"https://twitter.com/" + r.twitter_id + "\" target=_blank>" + "<img src=\"images/t.png\" style=\"width:30px; height:30px; margin-right:10px;\"/>" + "</a>";
        }
        if (r.facebook_id) {
            text += "<a href=\"https://facebook.com/" + r.facebook_id + "\" target=_blank>" + "<img src=\"images/f.png\" style=\"width:30px; height:30px; margin-right:10px;\"/>" + "</a>";
        }
        if (r.website) {
            text += "<a href=\"" + r.website + "\" target=_blank>" + "<img src=\"images/w.png\" style=\"width:30px; height:30px; margin-right:10px;\"/>" + "</a>";
        }
        text += "</td>";
        text += "</tr>";


        text += "</table>";

        more = "";
        more += "<h2 style=\"margin-top:0px;\">Committees</h2>"
        more += "<table class=\"table table-content details\" id=\"content\">";
        more += "<thead> <th>Chamber</th> <th>Committee ID</th> <th class=\"hidden-xs\"> Name </th></thead>";

        for (var i = 0; i < jsonData.committees.results.length; i++) {
            var r = jsonData.committees.results[i];
            more += "<tr>";
            more += "<td class=\"col-md-1\">" + r.chamber.charAt(0).toUpperCase() + r.chamber.slice(1) + "</td>";
            more += "<td class=\"col-md-2\">" + r.committee_id + "</td>";
            more += "<td class=\"col-md-6 hidden-xs\">" + r.name + "</td>";
            more += "</tr>";
        }
        more += "</table>";

        more += "<h2>Bills</h2>"
        more += "<table class=\"table table-content details\" id=\"content\">";
        more += "<thead> <th>Bill ID</th><th class=\"hidden-xs\">Title</th><th class=\"hidden-xs\">Chamber</th><th class=\"hidden-xs\">Bill Type</th> <th class=\"hidden-xs\">Congress</th> <th> Link </th></thead>";

        if (jsonData.bills) {
            for (var i = 0; i < jsonData.bills.results.length; i++) {
                var r = jsonData.bills.results[i];
                more += "<tr>";
                more += "<td class=\"col-md-1\">" + r.bill_id.toUpperCase() + "</td>";
                more += "<td class=\"col-md-6 hidden-xs \">" + r.official_title + "</td>";
                more += "<td class=\"col-md-1 hidden-xs \">" + r.chamber.charAt(0).toUpperCase() + r.chamber.slice(1) + "</td>";
                more += "<td class=\"col-md-1 hidden-xs \">" + r.bill_type.toUpperCase() + "</td>";
                more += "<td class=\"col-md-1 hidden-xs \">" + r.congress + "</td>";
                if (r.last_version && r.last_version.urls && r.last_version.urls.pdf) {
                    more += "<td class=\"col-md-1\">" + "<a href=\"" + r.last_version.urls.pdf + "\" target=\"_blank\">Link</a>" + "</td>";
                } else {
                    more += "<td class=\"col-md-1\">N.A.</td>";
                }
                more += "</tr>";
            }
        }
        more += "</table>";

        $('#object_div').html(more);

    } else if (database == 1) {
        r = jsonData.results[0];
        text += "<table class=\"table table-content details\" id=\"content\">";
        text += "<tr>";
        text += "<th class=\"col-md-1\"> Bill ID </th>";
        text += "<td class=\"col-md-12\">" + r.bill_id.toUpperCase() + "</td>";
        text += "</tr>";

        text += "<tr>";
        text += "<th class=\"col-md-1\"> Title </th>";
        text += "<td class=\"col-md-12\">" + r.official_title + "</td>";
        text += "</tr>";

        text += "<tr>";
        text += "<th class=\"col-md-1\"> Bill Type </th>";
        text += "<td class=\"col-md-12\">" + r.bill_type.toUpperCase() + "</td>";
        text += "</tr>";

        text += "<tr>";
        text += "<th class=\"col-md-1\"> Sponsor </th>";
        text += "<td class=\"col-md-12\">" + r.sponsor.title + ". " + r.sponsor.last_name + ", " + r.sponsor.first_name + "</td>";
        text += "</tr>";

        text += "<tr>";
        text += "<th class=\"col-md-1\"> Chamber </th>";
        text += "<td class=\"col-md-12\">" + r.chamber.charAt(0).toUpperCase() + r.chamber.slice(1) + "</td>";
        text += "</tr>";

        status = "New";
        filter = "new";
        if (r.history.active) {
            status = "Active";
            filter = "active";
        }

        text += "<tr>";
        text += "<th class=\"col-md-1\"> Status </th>";
        text += "<td class=\"col-md-3\">" + status + "</td>";
        text += "</tr>";

        text += "<tr>";
        text += "<th class=\"col-md-1\"> Introduced On </th>";
        text += "<td class=\"col-md-3\">" + r.introduced_on + "</td>";
        text += "</tr>";

        text += "<tr>";
        text += "<th class=\"col-md-1\"> Congress URL </th>";
        congress = "N.A.";
        if(r.urls && r.urls.congress) {
            congress = r.urls.congress;
            text += "<td class=\"col-md-2\"><a href=\"" + congress + "\" target=\"_blank\">URL</a></td>";
        }
        else {
            text += "<td class=\"col-md-2\">N.A.</td>";            
        }
        text += "</tr>";

        version = "N.A.";
        if(r.last_version && r.last_version.version_name) {
            version = r.last_version.version_name;
        }
        text += "<tr>";
        text += "<th class=\"col-md-1\"> Version Status </th>";
        text += "<td class=\"col-md-2\">" + version + "</td>";
        text += "</tr>";

        pdf_link = "#";
        if (r.last_version && r.last_version.urls && r.last_version.urls.pdf) {
            pdf_link = r.last_version.urls.pdf ;
        }
        text += "<tr>";
        text += "<th class=\"col-md-1\"> Bill URL </th>";
        text += "<td class=\"col-md-2\"><a href=\"" + pdf_link + "\" target=\"_blank\">Link</a></td>";
        text += "</tr>";

        text += "</table>";

        if (r.last_version && r.last_version.urls && r.last_version.urls.pdf) {
            pdf = "<object class=\"\" type=\"application/pdf\" width=100% height=450px data=\"" + r.last_version.urls.pdf + "\"> </object>";
            $('#object_div').html(pdf);
        }
    } else {
        alert('ERROR!');
        return;
    }


    return text;
}

function next_slide() {
    $('.carousel').carousel('next');
}

function prev_slide() {
    $('.carousel').carousel('prev');
}


function addToFavourite(database, id) {

    key = database + "_" + id;

    if(localStorage[database]) {
        arr = localStorage[database].split(',');
        index = $.inArray(id, arr);
        if(index != -1) {
            arr.splice(index, 1);
            $('#fav_' + key).remove();
        }
        else {
            arr.push(id);
            updateFavPanel(database, id);
        }
    }
    else {
        arr = [];
        arr.push(id);
        updateFavPanel(database, id);
    }

    localStorage[database] = arr;

    updateFavIcon(database, id);
}

function updateFavPanel(database, id) {
    $.ajax({
        url: "congress.php",
        dataType: "json",
        data: {
            database: database,
            id: id
        },
        success: function(data) {
            if (data.results.length > 0) {
                prepareAddRow(data, database, id);
            } else {
                alert('Something went wrong! Please try again!');
            }
        }
    });
}



function prepareAddRow(jsonData, database, id) {

    text = "";

    house = "<img src=\"images/h.png\" alt=\"Image not available\" width=30 height=30/>&nbsp;";
    senate = "<img src=\"images/s.svg\" alt=\"Image not available\" width=30 height=30/>&nbsp;";

    if (database == 0) {
        r = jsonData.results[0];
        text += "<tr id=\"fav_" + database + "_" + r.bioguide_id + "\">";
        text += "<td class=\"col-md-1\">"+
                "<a href=\"javascript:addToFavourite(" + database + ", '" + r.bioguide_id+ "')\">"+
                "<button id=\"favIcon\" type=\"button\" class=\"btn btn-default\">"+
                "<span class=\"glyphicon glyphicon-trash\"></span>"+
                "</button>"+
                "</a>"+
                "</td>";
        text += "<td class=\"col-md-2\"> <img src=\"http://theunitedstates.io/images/congress/original/" + r.bioguide_id + ".jpg\" style=\" width:50px; height:60px;\"/> </td>";
        text += "<td class=\"col-md-2\"> <img src=\"images/"+ r.party.toLowerCase().trim() +".png\" width=30 height=30 alt=\"Image Not Available\"/> </td>";
        text += "<td class=\"col-md-4\">" + r.last_name + ", " + r.first_name + "</td>";

        img = "";
        if (r.chamber == "house") {
            img = house;
        } else {
            img = senate;
        }

        text += "<td class=\"col-md-2\">" + img + r.chamber.charAt(0).toUpperCase() + r.chamber.slice(1) + "</td>";
        text += "<td class=\"col-md-2\">" + r.state_name + "</td>";

        if (r.oc_email) {
            text += "<td class=\"col-md-4\"><a href=\"mailto:" + r.oc_email + "\">" + r.oc_email + "</a></td>";
        } else {
            text += "<td>No Email Available</td>";
        }

        text += "<td class=\"col-md-2\"> <a href=\"javascript:viewDetails(0, '" + r.bioguide_id +"');\" class=\"twhite\">" + 
                "<button class=\"btn btn-primary twhite\">View Details</button></a> </td>";

        text += "</tr>";

        $('#3_fav_leg_table tr:last-child').after(text);

    } else if (database == 1) {
        r = jsonData.results[0];
        text += "<tr id=\"fav_" + database + "_" + r.bill_id + "\">";
        text += "<td class=\"col-md-1\">"+
                "<a href=\"javascript:addToFavourite(" + database + ", '" + r.bill_id+ "')\">"+
                "<button id=\"favIcon\" type=\"button\" class=\"btn btn-default\">"+
                "<span class=\"glyphicon glyphicon-trash\"></span>"+
                "</button>"+
                "</a>" +
                "</td>";
        text += "<td class=\"col-md-1\">" + r.bill_id.toUpperCase() + "</td>";
        text += "<td class=\"col-md-1\">" + r.bill_type.toUpperCase() + "</td>";
        text += "<td class=\"col-md-3\">" + r.official_title + "</td>";

        img = "";
        if (r.chamber == "house") {
            img = house;
        } else {
            img = senate;
        }

        text += "<td class=\"col-md-2\">" + img + r.chamber.charAt(0).toUpperCase() + r.chamber.slice(1) + "</td>";

        text += "<td class=\"col-md-2\">" + r.introduced_on + "</td>";

        text += "<td class=\"col-md-3\">" + r.sponsor.title + ". " + r.sponsor.last_name + ", " + r.sponsor.first_name + "</td>";
        text += "<td class=\"col-md-2\"> <a href=\"javascript:viewDetails(1, '" + r.bill_id +"');\" class=\"twhite\">" + 
                "<button class=\"btn btn-primary twhite\">View Details</button></a> </td>";

        text += "</tr>";

        $('#3_fav_bil_table tr:last-child').after(text);

    } else if (database == 2) {

        r = jsonData.results[0];
        text += "<tr id=\"fav_" + database + "_" + r.committee_id + "\">";
        text += "<td class=\"col-md-1\">"+
                "<a href=\"javascript:addToFavourite(" + database + ", '" + r.committee_id+ "')\">"+
                "<button id=\"favIcon\" type=\"button\" class=\"btn btn-default\">"+
                "<span class=\"glyphicon glyphicon-trash\"></span>"+
                "</button>"+
                "</a>" +
                "</td>";

        img = "";
        if (r.chamber == "house") {
            img = house;
        } else {
            img = senate;
        }

        text += "<td class=\"col-md-1\">" + img + r.chamber.charAt(0).toUpperCase() + r.chamber.slice(1) + "</td>";

        text += "<td class=\"col-md-1\">" + r.committee_id.toUpperCase() + "</td>";

        text += "<td class=\"col-md-4\">" + r.name + "</td>";

        var parent_committee = "";

        if (r.parent_committee_id) {
            parent_committee = r.parent_committee_id;
        }

        text += "<td class=\"col-md-2\">" + parent_committee + "</td>";

        text += "<td class=\"col-md-2\">" + r.subcommittee + "</td>";

        text += "</tr>";


        $('#3_fav_com_table tr:last-child').after(text);

    } else {
        alert('ERROR!');
        return;
    }


    return text;
}

function updateFavIcon(database, id) {
    key = database + "_" + id;
    flag = false;
    if(localStorage[database]) {
        arr = localStorage[database].split(',');
        index = $.inArray(id, arr);
        if(index != -1) {
            flag = true;
        }
    }

    if (flag) {
        $('#' + key).removeClass('glyphicon-star-empty').addClass('glyphicon-star').addClass('yellow');
    } else {
        $('#' + key).addClass('glyphicon-star-empty').removeClass('glyphicon-star').removeClass('yellow');
    }


}