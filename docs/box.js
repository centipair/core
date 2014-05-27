var box_types = ["html",
		 "markdown",
		 "text",
		 "url",
		 "image",
		 "video"
		 "youtube",
		 "event",
		 ""
		]


var api = {version: "1.0"
	   boxes:[{box_id:"timeuuid"
		   name: "My awesome box",
		   url: "http://domain.com/url",
		   owner: "timeuuid"
		   created_date: ""
		  },
		 {name: "Another awesome box",
		  url: "http://newdomain.com/anotherurl",
		  owner: "timeuuid",
		  created_date: "",
		  }]
	   
	  }

var box = {box_id
	   name: "Awesome box"
	   feed: [{title:"Some nice title", /* 1024 chars */
		   type: "html markdown text url image(url) youtube..etc",
		   content: "content html markdown url image(url) youtube(url) event anything...etc",
		   box_id:"timeuuid",
		   feed_id:"timeuuid",
		   created_date:"",
		   edited_date:""}]}
