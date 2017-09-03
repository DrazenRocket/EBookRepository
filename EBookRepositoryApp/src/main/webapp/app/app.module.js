(function (angular) {
    "use strict";

    angular
        .module("app", ["ui.router", "app.user", "app.category", "app.language", "app.eBook", "app.core"]);  // TODO: Check required modules
} (angular));
