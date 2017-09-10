(function (angular) {
    "use strict";

    angular
        .module("app")
        .config(configRoute);

    configRoute.$inject = ["$locationProvider", "$urlRouterProvider", "$stateProvider"];
    function configRoute($locationProvider, $urlRouterProvider, $stateProvider) {
        $locationProvider.hashPrefix("!");
        $urlRouterProvider.otherwise("/e-book-search");
    }
} (angular));
