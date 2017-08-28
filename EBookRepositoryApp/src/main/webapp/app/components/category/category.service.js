(function (angular) {
    "use strict";

    angular
        .module("app.category")
        .service("categoryService", CategoryService);

    CategoryService.$inject = ["$http"];
    function CategoryService($http) {
        this.$http = $http;
    }

    CategoryService.prototype.getAllCategories = function () {
        var request = {
            method: "GET",
            url: "/api/categories"
        };

        return this.$http(request);
    };
} (angular));
