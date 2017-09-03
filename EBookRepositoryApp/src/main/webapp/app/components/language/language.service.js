(function (angular) {
    "use strict";

    angular
        .module("app.language")
        .service("languageService", LanguageService);

    LanguageService.$inject = ["$http"];
    function LanguageService($http) {
        this.$http = $http;
    }

    LanguageService.prototype.getAllLanguages = function () {
        var request = {
            method: "GET",
            url: "/api/languages"
        };

        return this.$http(request);
    };
} (angular));
