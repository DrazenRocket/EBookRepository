(function (angular) {
    "use strict";

    angular
        .module("app.eBook")
        .service("eBookService", EBookService);

    EBookService.$inject = ["$http", "$window", "userService"];
    function EBookService($http, $window, userService) {
        this.$http = $http;
        this.$window = $window;
        this.userService = userService;
    }

    EBookService.prototype.getEBookById = function (eBookId) {
        var request = {
            method: "GET",
            url: "/api/ebooks/" + eBookId
        };

        return this.$http(request);
    };

    EBookService.prototype.getAllEBooks = function () {
        var request = {
            method: "GET",
            url: "/api/ebooks"
        };

        return this.$http(request);
    };

    EBookService.prototype.getAllEBooksByCategoryId = function (categoryId) {
        var request = {
            method: "GET",
            url: "/api/categories/" + categoryId + "/ebooks"
        };

        return this.$http(request);
    };

    EBookService.prototype.getEBookFileByEBookId = function (eBookId) {
        var jwt = this.userService.getJwtFromLocalStorage();
        var request = {
            method: "GET",
            url: "/api/ebooks/" + eBookId + "/file",
            headers: {
                "X-Auth-Jwt": jwt
            },
            responseType: "arraybuffer"
        };

        return this.$http(request);
    };

    EBookService.prototype.downloadEBookFileByEBookId = function (eBookId) {
        var thisEBookService = this;

        this.getEBookFileByEBookId(eBookId)
            .then(function (response) {
                if (response.status == 200) {
                    var data = response.data;
                    var dataBlob = new Blob([data], {type: 'application/pdf'});
                    var aElement = document.createElement("a");                         // Try to use something generic
                    var fileUrl = window.URL.createObjectURL(dataBlob);                 //

                    aElement.href = fileUrl;
                    aElement.download = "EBook.pdf";
                    aElement.click();
                }
            }, function (response) {
                if (response.status == 404) {
                    thisEBookService.$window.alert("File can't be found.");
                }
                else {
                    thisEBookService.$window.alert("File can't be downloaded.");
                }
            });
    };
} (angular));
