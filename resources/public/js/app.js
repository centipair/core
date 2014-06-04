var app = angular.module("app", ['ngRoute', 'ui.bootstrap', 'angularFileUpload']);

app.config(function($interpolateProvider, $httpProvider) {
    $interpolateProvider.startSymbol('[{');
    $interpolateProvider.endSymbol('}]');
    
});

function makeUrl(url, params){
    return url+params.id;
}

app.factory("Notifier", function(){
    return {show:false, message:"Loading...", class:"notify-loading"};
});

app.service("Callback", function(){
    this.registrationSuccess = function(data){
	console.log(data);
    };
});

app.service("PostData", function($http, $q, Notifier){
    
    return {submitForm: function(url, data){
	var deferred = $q.defer();
	$http(
	    {url: url, 
	     data: data,
	     method: 'POST'
	     
	     //headers : {'Content-Type':'application/x-www-form-urlencoded; charset=UTF-8',
	     //"X-Requested-With":"XMLHttpRequest"}
	    }).error(function (data, status) {
		response = {data: data, status: status};
		deferred.reject(response);
		
	    }).success(function (data){
		response = {data:data, status:200};
		deferred.resolve(response);
	    });
	return deferred.promise;
    }};
});

app.service("GetData", function($http, $q, Notifier){
    return {getData:function(url, data){
	var deferred = $q.defer();
	$http(
	    {url: url, 
	     data: data,
	     method: 'GET',
	     headers : {'Content-Type':'application/x-www-form-urlencoded; charset=UTF-8'}
	    }).error(function (data, status) {
		response = {data: data, status: status};
		deferred.reject(response);
		
	    }).success(function (data){
		response = {data:data, status:200};
		deferred.resolve(response);
	    });
	return deferred.promise;
	
    }};
});


app.directive("submit", function(){
    return function (scope, element, attrs){
	element.bind("click", function (){
	    scope.submitFormService(attrs.url);
	})
    }
});


function NotifierCtrl($scope, Notifier){
    $scope.notifier = Notifier;
    $scope.loaderMessage = "Loading";
    $scope.notify = function(code, message){
	var show = true;
	switch (code)
	{
	    case 102:
	    Notifier.class = "notify-loading";
	    Notifier.message = $scope.loaderMessage
	    break;
	    case 404:
	    Notifier.class = "notify-error";
	    Notifier.message = "Requested resource not found";
	    break;
	    case 500:
	    Notifier.class = "notify-error";
	    Notifier.message = "Server error. Please try again after sometime";
	    break;
	    case 422:
	    Notifier.class = "notify-error";
	    Notifier.message = "Submitted data is invalid";
	    break;
	    case 403:
	    Notifier.class = "notify-error";
	    Notifier.message = "Permission denied for this process";
	    break;
	    case 200:
	    show = false;
	    break;
	    default:
	    Notifier.class = "notify-loading";
	    Notifier.message = "Unknown response";
	    
	}
	if (message != undefined){
	    Notifier.message = message;
	}
	Notifier.show = show;
	
    };
    $scope.hideNotification = function(){
	Notifier.show = false;
	Notifier.message = "";
    };
}


app.controller('SubmitCtrl', function($scope, $controller, $http, PostData, GetData){
    $controller('NotifierCtrl', {$scope:$scope});
    $scope.errors = {};
    $scope.form = {}
    $scope.allErrors = false;
    $scope.data = {};
    $scope.submitButton={disabled: false};
    $scope.callback=function(data){
	
    };
    $scope.getCallback=function(data){
    };
    $scope.serialize = function(obj, prefix) {
	var str = [];
	for(var p in obj) {
	    var k = prefix ? prefix + "[" + p + "]" : p, v = obj[p];
	    str.push(typeof v == "object" ?
		     serialize(v, k) :
		     encodeURIComponent(k) + "=" + encodeURIComponent(v));
	}
	return str.join("&");
    };
    $scope.submitFormService=function(url){
	$scope.submitButton.disabled = true;
	$scope.submitButton.text = 'Submitting';
	$scope.notify(102, $scope.loaderMessage)
	$scope.errors = {};
	$scope.successMessage = "";
	submit_data = $scope.form;
	submit_data["csrfmiddlewaretoken"] = document.getElementById('csrfmiddlewaretoken').value;
	$scope.response  = PostData.submitForm(url, submit_data).then(
	    function (response){
		//this is success data
		$scope.submitButton.disabled=false;
		$scope.notify(200)
		$scope.callback(response.data);
	    },
	    function (response){
		//this is invoked during error
		console.log(response);
		$scope.submitButton.disabled=false;
		data = response.data;
		status = response.status;
		if (status==422){
		    $scope.notify(422, data.message)
		    errors = {};
		    for(var index in data.errors) {
			errors[index + "Class"] = "has-error";
			errors[index] = data.errors[index];
		    }
		    $scope.errors = errors;
		    $scope.allErrors = '__all__' in data.errors;
		    
		}else if (status==500){
		    $scope.notify(500);
		}
		else if(status==404){
		    $scope.notify(404);
		}else if(status==403){
		    $scope.notify(403);
		}
		
	    }
	);//posdata service ends
	
    };
    $scope.getDataService = function(url, data){
	$scope.notify(102, "Loading...");
	$scope.getData  = GetData.getData(url).then(
	    function (response){
		//this is success data
		$scope.notify(200);
		$scope.getCallback(response.data);
	    },
	    function (response){
		//this is invoked during error
		data = response.data;
		status = response.status;
		
	    }
	);
    };
    
});




app.controller('ComposeCtrl', function($scope, $controller, $upload){
    $controller('SubmitCtrl', {$scope:$scope});
    /*Date time*/
    $scope.mytime = new Date();
    
    $scope.hstep = 1;
    $scope.mstep = 1;
    
    $scope.options = {
	hstep: [1, 2, 3],
	mstep: [1, 5, 10, 15, 25, 30]
    };
    
  $scope.ismeridian = true;
    $scope.toggleMode = function() {
	$scope.ismeridian = ! $scope.ismeridian;
    };
    
    $scope.update = function() {
	var d = new Date();
	d.setHours( 14 );
	d.setMinutes( 0 );
	$scope.mytime = d;
    };

    $scope.changed = function () {
	console.log('Time changed to: ' + $scope.mytime);
    };
    
    $scope.clear = function() {
	$scope.mytime = null;
    };
    /*Date Time Ends*/
    $scope.loaderMessage = "Submitting post";
    $scope.location = {};
    $scope.event_location = {};
    $scope.post = {
	content: "",
	content_raw:"",
	url:"",
	location_name:"",
	location_latitude:"",
	location_longitude:"",
	event_name:"",
	event_description:"",
	event_date:"",
	event_time:""}
    $scope.open = function($event) {
	$event.preventDefault();
	$event.stopPropagation();
	
	$scope.opened = true;
    };

    //initially no widget is shown
    $scope.widgets = {image:false,
		      youtube:false,
		      url:false,
		      location:false,
		      event:false
		     }
    $scope.showWidget = function(widget){
	$scope.widgets = {image:false,
			  youtube:false,
			  url:false,
			  location:false,
			  event:false
			 }
	$scope.widgets[widget] = true;
	
    }

    $scope.submitPost = function(){
	//console.log($scope.mytime.getTime());
	text = $scope.post.content.replace(/<br\/>/g, '\n');
	text = text.replace(/<br>/g, '\n');
	
	$scope.post.content_raw = text;
	$scope.post.location_name = $scope.location.name;
	$scope.post.location_latitude = $scope.location.latitude;
	$scope.post.location_longitude = $scope.location.longitude;
	$scope.post.location_address = $scope.location.address;
	$scope.form = $scope.post;
	//console.log($scope.form);
	$scope.submitFormService('/feed/post');
    };
    
    $scope.callback = function(data){
	console.log(data);
    };
     $scope.onFileSelect = function($files) {
    //$files: an array of files selected, each file has name, size, and type.
	 for (var i = 0; i < $files.length; i++) {
	     var file = $files[i];
	     $scope.upload = $upload.upload({
		 url: '/feed/upload', //upload.php script, node.js route, or servlet url
		 // method: 'POST' or 'PUT',
		 // headers: {'header-key': 'header-value'},
		 // withCredentials: true,
		 data: {myObj: $scope.post.id},
		 file: file, // or list of files: $files for html5 only
		 /* set the file formData name ('Content-Desposition'). Default is 'file' */
		 //fileFormDataName: myFile, //or a list of names for multiple files (html5).
		 /* customize how data is added to formData. See #40#issuecomment-28612000 for sample code */
		 //formDataAppender: function(formData, key, val){}
	     }).progress(function(evt) {
		 console.log('percent: ' + parseInt(100.0 * evt.loaded / evt.total));
	     }).success(function(data, status, headers, config) {
		 // file is uploaded successfully
		 console.log(data);
	     });
	     //.error(...)
	     //.then(success, error, progress); 
	     //.xhr(function(xhr){xhr.upload.addEventListener(...)})// access and attach any event listener to XMLHttpRequest.
	 }
	 /* alternative way of uploading, send the file binary with the file's content-type.
       Could be used to upload files to CouchDB, imgur, etc... html5 FileReader is needed. 
       It could also be used to monitor the progress of a normal http post/put request with large data*/
	 // $scope.upload = $upload.http({...})  see 88#issuecomment-31366487 for sample code.
     };
});




app.directive('contenteditable', function() {
    return {
      restrict: 'A', // only activate on element attribute
      require: '?ngModel', // get a hold of NgModelController
      link: function(scope, element, attrs, ngModel) {
	  
          if(!ngModel) return; // do nothing if no ng-model

          // Specify how UI should be updated
          ngModel.$render = function() {
              element.html(ngModel.$viewValue || '');
          };
	  
          //read(); // initialize
	  clean(); //initialize
	  element.bind('paste', function(e){
	      setTimeout(function() {
		  scope.$apply(clean);
	      });
	      
              
          });
	  element.bind('keyup', function(e){
	      setTimeout(function() {
		  scope.$apply(change);
	      });
	      
              
          });

	  function change(){
	      var text = element.html();
	      //text = text.replace(/\n/g, '<br/>');
	      ngModel.$setViewValue(text);
	      
	  }

	  function clean(){
	      var init_text = document.getElementById(attrs.id).innerHTML;
	      init_text = init_text.replace(/<br\/>/g, '\n');
	      init_text = init_text.replace(/<br>/g, '\n');
	      document.getElementById(attrs.id).innerHTML = init_text;
	      var text = element.text();
	      text = text.replace(/\n/g, '<br/>');
	      //console.log(text);
	      document.getElementById(attrs.id).innerHTML = text;
	      ngModel.$setViewValue(text);
	  }
	  
	  
          // Write data to the model
          function read() {
	      //console.log("reading");
              var html = element.html();
              // When we clear the content editable the browser leaves a <br> behind
              // If strip-br attribute is provided then we strip this out
              if( attrs.stripBr && html == '<br>' ) {
		  html = '';
              }
              ngModel.$setViewValue(html);
        }
      }
    };
  });



 /* Directives */

app.directive('googlePlaces', function(){
    return {
        restrict:'E',
        replace:true,
        // transclude:true,
        scope: {location:'='},
        template: '<input id="google_places_ac" name="google_places_ac" type="text" class="form-control" ng-model="post.location_name"/>',
        link: function($scope, elm, attrs){
            var autocomplete = new google.maps.places.Autocomplete(document.getElementById("google_places_ac"), {});
            google.maps.event.addListener(autocomplete, 'place_changed', function() {
                var place = autocomplete.getPlace();
		console.log(place);
		var location = {}
		
		location.name = place.name;
		location.address = place.formatted_address;
		location.latitude = place.geometry.location.lat();
		location.longitude = place.geometry.location.lng();
                //$scope.location = place.geometry.location.lat() + ',' + place.geometry.location.lng();
		$scope.location = location;
                $scope.$apply();
            });
            }
    }
});
