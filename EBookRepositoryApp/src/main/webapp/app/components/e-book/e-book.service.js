(function (angular) {
    "use strict";

    angular
        .module("app.eBook")
        .service("eBookService", EBookService);

    function EBookService($http) {
        this.$http = $http;
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
} (angular));
