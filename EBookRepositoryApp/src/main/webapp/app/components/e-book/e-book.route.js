(function (angular) {
    "use strict";

    angular
        .module("app.eBook")
        .config(configRoute);

    configRoute.$inject = ["$stateProvider"];
    function configRoute($stateProvider) {
        $stateProvider
            .state("e-book-list", {
                url: "/e-book-list",
                views: {
                    headerView: {
                        templateUrl: "app/components/core/core-header/core-header.html",
                        controller: "CoreHeaderController",
                        controllerAs: "coreHeaderViewModel"
                    },
                    mainView: {
                        templateUrl: "app/components/e-book/e-book-list/e-book-list.html",
                        controller: "EBookListController",
                        controllerAs: "eBookListViewModel"
                    }
                }
            })
            .state("e-book-detail", {
                url: "/e-book-detail/:eBookId",
                views: {
                    headerView: {
                        templateUrl: "app/components/core/core-header/core-header.html",
                        controller: "CoreHeaderController",
                        controllerAs: "coreHeaderViewModel"
                    },
                    mainView: {
                        templateUrl: "app/components/e-book/e-book-detail/e-book-detail.html",
                        controller: "EBookDetailController",
                        controllerAs: "eBookDetailViewModel"
                    }
                }
            });
    }
} (angular));
