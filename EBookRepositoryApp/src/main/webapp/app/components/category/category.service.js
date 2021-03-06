(function (angular) {
    "use strict";

    angular
        .module("app.category")
        .service("categoryService", CategoryService);

    CategoryService.$inject = ["userService", "$http"];
    function CategoryService(userService, $http) {
        this.userService = userService;
        this.$http = $http;
    }

    CategoryService.prototype.getCategoryById = function (categoryId) {
        var request = {
            method: "GET",
            url: "/api/categories/" + categoryId
        };

        return this.$http(request);
    };

    CategoryService.prototype.getAllCategories = function () {
        var request = {
            method: "GET",
            url: "/api/categories"
        };

        return this.$http(request);
    };

    CategoryService.prototype.addCategory = function (name) {
        var jwt = this.getJwtFromLocalStorage();
        var request = {
            method: "POST",
            url: "/api/categories",
            headers: {
                "X-Auth-Jwt": jwt
            },
            data: {
                name: name
            }
        };

        return this.$http(request);
    };

    CategoryService.prototype.editCategory = function (id, name) {
        var jwt = this.getJwtFromLocalStorage();
        var request = {
            method: "PUT",
            url: "/api/categories/" + id,
            headers: {
                "X-Auth-Jwt": jwt
            },
            data: {
                name: name
            }
        };

        return this.$http(request);
    };
} (angular));
