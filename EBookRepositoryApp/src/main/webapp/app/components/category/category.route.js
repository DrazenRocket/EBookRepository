(function (angular) {
    "use strict";

    angular
        .module("app.category")
        .config(configRoute);

    configRoute.$inject = ["$stateProvider"];
    function configRoute($stateProvider) {
        $stateProvider
            .state("category-list", {
                url: "/category-list",
                views: {
                    headerView: {
                        templateUrl: "app/components/core/core-header/core-header.html",
                        controller: "CoreHeaderController",
                        controllerAs: "coreHeaderViewModel"
                    },
                    mainView: {
                        templateUrl: "app/components/category/category-list/category-list.html",
                        controller: "CategoryListController",
                        controllerAs: "categoryListViewModel"
                    }
                }
            });
    }
} (angular));
