<?php

    // $base_host = "http://104.198.0.197:8080/";
    // $base_local_host = "http://104.198.0.197:8080/";
    // $url = "http://104.198.0.197:8080/";
    // $local_url = "http://104.198.0.197:8080/";
    $base_host = "http://104.198.0.197:8080/";
    $base_local_host = "http://104.198.0.197:8080/";
    $url = "http://104.198.0.197:8080/";
    $local_url = "http://104.198.0.197:8080/";
    $api_key = "&apikey=4186cea6482f48959f340a5dd7cd2080";
    $page = "&per_page=all";

    function get_json_data($qurl) {

        $ctx = stream_context_create(array(
            'http' => array(
                'timeout' => 90,
                'method' => 'GET',
                )
            )
        );

        //$local_qurl = str_replace("//congress.api.sunlightfoundation.com/", "//104.198.0.197:8080/", $qurl);
        $local_qurl = $qurl;
        // $raw_json = file_get_contents($qurl, 0, $ctx);
        // if($raw_json === false) {
        //     $raw_json = file_get_contents($local_qurl, 0, $ctx);
        // }
        $raw_json = file_get_contents($local_qurl, 0, $ctx);
        // if($raw_json === false) {
        //     //$raw_json = file_get_contents();get from local file;
        // }
        $decoded_data = json_decode($raw_json, TRUE);
        return json_encode($decoded_data);
    }
    
    if($_GET["database"] == "0") {
        if(isset($_GET["filter"])) {
            $f = $_GET["filter"];
            if($f == "house") {
                $leg_house_url = $url . "legislators?chamber=house" . $api_key . $page;
                print_r(get_json_data($leg_house_url)); 
            }
            else if($f == "senate") {
                $leg_senate_url = $url . "legislators?chamber=senate" . $api_key . $page;
                print_r(get_json_data($leg_senate_url)); 
            }
            else {
                $leg_url = $url . "legislators?per_page=all" . $api_key;
                print_r(get_json_data($leg_url)); 
            }
        }

        else if(isset($_GET["id"])) {
            $url = $url . "legislators?bioguide_id=" . $_GET["id"];

            $com_url = $base_host . "committees?member_ids=" . $_GET["id"] . $api_key . "&per_page=5";

            $com_json = get_json_data($com_url);
            $com_data = json_decode($com_json,true);

            $r_arr = $com_data['results'];

            $bill_url = $base_host . "bills?sponsor_id=" . $_GET["id"] . $api_key . "&per_page=5";

            $bill_json = get_json_data($bill_url);
            $bill_data = json_decode($bill_json,true);

            $url = $url . $api_key . $page;
            $json = get_json_data($url); 
            $data = json_decode($json,true);
            $data['committees'] = $com_data;
            $data['bills'] = $bill_data;

            print_r(json_encode($data));
        }

    }

    else if($_GET["database"] == "1") {
        if(isset($_GET["filter"])) {
            $f = $_GET["filter"];
            if($f == "active") {
                $bill_url = $url . "bills?" . $api_key . "&per_page=51&history.active=true&last_version.urls.pdf__exists=true";
                print_r(get_json_data($bill_url)); 
            }
            else if($f == "new") {
                $bill_new_url = $url . "bills?" . $api_key . "&per_page=51&history.active=false&last_version.urls.pdf__exists=true";
                print_r(get_json_data($bill_new_url)); 
            }
        }

        else if(isset($_GET["id"])) {
            if($bill_cache[$_GET["id"]]) {
                $rd = array();
                $rd['results'] = [$bill_cache[$_GET["id"]]];
                print_r(json_encode($rd));
            }
            else {
                $url = $url . "bills?bill_id=" . $_GET["id"] . "&";
                $url = $url . $api_key . $page;
                $json = get_json_data($url); 

                print_r($json);
            }
        }
    }

    else if($_GET["database"] == "2") {
        if(isset($_GET["filter"])) {
            $f = $_GET["filter"];
            if($f == "house") {
                $committee_url = $url . "committees?chamber=house" . $api_key . $page;
                print_r(get_json_data($committee_url)); 
            }
            else if($f == "senate") {
                $committee_senate_url = $url . "committees?chamber=senate" . $api_key . $page;
                print_r(get_json_data($committee_senate_url)); 
            }
            else if($f == "joint") {
                $committee_joint_url = $url . "committees?chamber=joint" . $api_key . $page;
                print_r(get_json_data($committee_joint_url)); 
            }
        }

        else if(isset($_GET["id"])) {
            $url = $url . "committees?committee_id=" . $_GET["id"] . "&";
            $url = $url . $api_key . $page;
            $json = get_json_data($url); 

            print_r($json);
        }

        else {
            print_r($committee_data);
        }
    }

?>