app.controller('EarlyAccessCtrl', function($scope, $controller){
    $controller('SubmitCtrl', {$scope:$scope});
    $scope.loaderMessage = "Saving...";
    $scope.callback = function(data){
	$scope.successMessage = "Email saved. Thank you for your interest. We will notify you when we launch";
    }
});

app.controller('RegisterCtrl', function($scope, $controller){
    $controller('SubmitCtrl', {$scope:$scope});
    $scope.registrationForm=true;
    $scope.registrationSuccess=false;
    $scope.callback = function(data){
	$scope.registrationForm=false;
	$scope.registrationSuccess=true;
    }
});

app.controller('LoginCtrl', function($scope, $controller){
    $controller('SubmitCtrl', {$scope:$scope});
    $scope.loaderMessage = "Logging in...";
    $scope.callback = function(data){
	window.location = data.redirect;
    }
});

app.controller('ForgotPasswordCtrl', function($scope, $controller){
    $controller('SubmitCtrl', {$scope:$scope});
    $scope.loaderMessage = "Submitting...";
    $scope.callback = function(data){
	console.log(data);
    }
});
