app.controller('AdminCtrl', function($scope, $controller, $location, Page, $templateCache){
    $controller('SubmitCtrl', {$scope:$scope});
    $scope.loaderMessage = "Loading...";
    $scope.notify(102);
    $scope.page = Page;
    $scope.page.title = 'Admin';
    $scope.editUrl = '';
    $scope.$on('$routeChangeStart', function() {
	$scope.notify(102);
	//$templateCache.removeAll();
    });
    $scope.$on('$routeChangeSuccess', function() {
	$scope.hideNotification();
    });
    $scope.$on('$routeChangeError', function(data, status) {
	console.log(data.status);
	$scope.notify(404, "Page not found");
    });
    $scope.editData = function(object){
	$location.url($scope.editUrl+object.id);
    };
    
});

app.controller('MenuCtrl', function($scope, $location){
    $scope.getClass = function(path) {
	if ($location.path().substr(0, path.length) == path) {
	    return "active"
	} else {
	    return ""
	}
    }
});


app.controller('Admin404Ctrl', function($scope, $controller){
    $controller('AdminCtrl', {$scope:$scope});
    $scope.page.title = "Page not found"
    $scope.callback = function(data){
	console.log(data);
    }
});

app.controller('SiteEditCtrl', function($scope, $controller){
    $controller('AdminCtrl', {$scope:$scope});
    $scope.page.title = "Site controller"
    $scope.siteData = {};
    $scope.loaderMessage="Saving..";
    $scope.callback = function(data){
	console.log('Site Callback');
    }

});


app.controller('SitesCtrl', function($scope, $controller, $location){
    $controller('AdminCtrl', {$scope:$scope});
    $scope.page.title = "My sites"
    $scope.getDataService('/api/admin/site/list');
    $scope.editUrl = '/site/edit/';
    $scope.getCallback = function(data){
	$scope.listData = data;
    };
    $scope.editData = function(object){
	$location.url($scope.editUrl+object.site_id);
    };
});

app.controller('SiteEditCtrl', function($scope, $controller, $location){
    $controller('AdminCtrl', {$scope:$scope});
    $scope.page.title = "Add-Edit Site"
    $scope.getDataService('/api/admin/site/');
    $scope.editUrl = '/site/edit/';
    $scope.getCallback = function(data){
	$scope.listData = data;
    };
    $scope.editData = function(object){
	$location.url($scope.editUrl+object.site_id);
    };
});

app.controller('PageCtrl', function($scope, $controller){
    $controller('AdminCtrl', {$scope:$scope});
    $scope.page.title = "Page"
    $scope.siteData = {};
    $scope.getDataService('/api/admin/page/list');
    $scope.editUrl = '/admin/page/edit/';
    $scope.getCallback = function(data){
	$scope.pageData = data;
    }

});

app.controller('ProfileCtrl', function($scope, $controller){
    $controller('AdminCtrl', {$scope:$scope});
    $scope.page.title = "Page"
    $scope.getCallback = function(data){
	$scope.pageData = data;
    }

});

app.controller('HelpCtrl', function($scope, $controller){
    $controller('AdminCtrl', {$scope:$scope});
    $scope.page.title = "Help Centre"
    $scope.getCallback = function(data){
	$scope.pageData = data;
    }

});

app.controller('SettingsCtrl', function($scope, $controller){
    $controller('AdminCtrl', {$scope:$scope});
    $scope.page.title = "Settings"
    $scope.getCallback = function(data){
	$scope.pageData = data;
    }

});

app.controller('BusinessCtrl', function($scope, $controller){
    $controller('AdminCtrl', {$scope:$scope});
    $scope.page.title = "Business"
    $scope.getCallback = function(data){
	$scope.pageData = data;
    }

});
/*
app.service("CkEditor", function(){
    return {initEditor: function(id){
	

	CKEDITOR.replace(
	    id, 
	    {filebrowserBrowseUrl : '/login',
	     filebrowserImageBrowseUrl : '/login',
	     filebrowserFlashBrowseUrl : '/core-cdn/ckfinder/ckfinder.html?type=Flash',
	     filebrowserUploadUrl : '/file-upload',
	     filebrowserImageUploadUrl : '/admin/image-upload',
	     filebrowserFlashUploadUrl : '/core-cdn/ckfinder/core/connector/php/connector.php?command=QuickUpload&type=Flash'}
			      );
	return ;
    }};
    
    
    
});
*/

app.filter('urlize', function(){
    return function (text){
	return text.toLowerCase().replace(/[^\w ]+/g,'').replace(/ +/g,'-');
	//return "urlized? filter?";
    }
});

app.controller('PageEditCtrl', function($scope, $controller, CkEditor){
    $controller('AdminCtrl', {$scope:$scope});
    $scope.siteData = {};
    $scope.form = {url:"", title:""};
    $scope.loaderMessage="Saving..";
    $scope.callback = function(data){
	console.log('Page Callback');
    };
    $scope.$on('$routeChangeSuccess', function() {
	CkEditor.initEditor('description');
	
    });
    $scope.$watch('form.title', function(v){
	$scope.form.url = $scope.form.title.toLowerCase().replace(/[^\w ]+/g,'').replace(/ +/g,'-');
    });

});

app.controller('BlogListCtrl', function($scope, $controller){
    $controller('AdminCtrl', {$scope:$scope});
    $scope.page.title = "Blog"
    $scope.siteData = {};
    $scope.getDataService('/admin/cms/blog?action=list-data');
    $scope.editUrl = '/blog/edit/';
    $scope.getCallback = function(data){
	$scope.pageData = data;
    }

});


app.controller('BlogEditCtrl', function($scope, $controller){
    $controller('AdminCtrl', {$scope:$scope});
    $scope.page.title = "Blog"
    $scope.siteData = {};
    $scope.loaderMessage="Saving..";
    $scope.callback = function(data){
	console.log('Page Callback');
    }

});





app.factory("Page", function(){
    return {title: "Site Administration"};
});
