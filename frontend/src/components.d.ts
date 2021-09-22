/* eslint-disable */
/* tslint:disable */
/**
 * This is an autogenerated file created by the Stencil compiler.
 * It contains typing information for all components that exist in this project.
 */
import { HTMLStencilElement, JSXBase } from "@stencil/core/internal";
import { MatchResults, RouterHistory } from "@stencil/router";
import { MenuClickedEvent } from "./components/header/types";
export namespace Components {
    interface AppHome {
    }
    interface AppProfile {
        "match": MatchResults;
    }
    interface AppRoot {
        "history": RouterHistory;
    }
    interface CfHeader {
        "history": RouterHistory;
    }
    interface CfHeaderMenu {
        "history": RouterHistory;
    }
    interface CfLogin {
        "history": RouterHistory;
    }
    interface CfRegister {
        "history": RouterHistory;
    }
    interface GlobalRoleColumnGrants {
    }
    interface GlobalRoleMemberships {
    }
    interface GlobalRoleTablePermissions {
    }
    interface GlobalRoles {
    }
    interface TableRoot {
        "match": MatchResults;
    }
}
declare global {
    interface HTMLAppHomeElement extends Components.AppHome, HTMLStencilElement {
    }
    var HTMLAppHomeElement: {
        prototype: HTMLAppHomeElement;
        new (): HTMLAppHomeElement;
    };
    interface HTMLAppProfileElement extends Components.AppProfile, HTMLStencilElement {
    }
    var HTMLAppProfileElement: {
        prototype: HTMLAppProfileElement;
        new (): HTMLAppProfileElement;
    };
    interface HTMLAppRootElement extends Components.AppRoot, HTMLStencilElement {
    }
    var HTMLAppRootElement: {
        prototype: HTMLAppRootElement;
        new (): HTMLAppRootElement;
    };
    interface HTMLCfHeaderElement extends Components.CfHeader, HTMLStencilElement {
    }
    var HTMLCfHeaderElement: {
        prototype: HTMLCfHeaderElement;
        new (): HTMLCfHeaderElement;
    };
    interface HTMLCfHeaderMenuElement extends Components.CfHeaderMenu, HTMLStencilElement {
    }
    var HTMLCfHeaderMenuElement: {
        prototype: HTMLCfHeaderMenuElement;
        new (): HTMLCfHeaderMenuElement;
    };
    interface HTMLCfLoginElement extends Components.CfLogin, HTMLStencilElement {
    }
    var HTMLCfLoginElement: {
        prototype: HTMLCfLoginElement;
        new (): HTMLCfLoginElement;
    };
    interface HTMLCfRegisterElement extends Components.CfRegister, HTMLStencilElement {
    }
    var HTMLCfRegisterElement: {
        prototype: HTMLCfRegisterElement;
        new (): HTMLCfRegisterElement;
    };
    interface HTMLGlobalRoleColumnGrantsElement extends Components.GlobalRoleColumnGrants, HTMLStencilElement {
    }
    var HTMLGlobalRoleColumnGrantsElement: {
        prototype: HTMLGlobalRoleColumnGrantsElement;
        new (): HTMLGlobalRoleColumnGrantsElement;
    };
    interface HTMLGlobalRoleMembershipsElement extends Components.GlobalRoleMemberships, HTMLStencilElement {
    }
    var HTMLGlobalRoleMembershipsElement: {
        prototype: HTMLGlobalRoleMembershipsElement;
        new (): HTMLGlobalRoleMembershipsElement;
    };
    interface HTMLGlobalRoleTablePermissionsElement extends Components.GlobalRoleTablePermissions, HTMLStencilElement {
    }
    var HTMLGlobalRoleTablePermissionsElement: {
        prototype: HTMLGlobalRoleTablePermissionsElement;
        new (): HTMLGlobalRoleTablePermissionsElement;
    };
    interface HTMLGlobalRolesElement extends Components.GlobalRoles, HTMLStencilElement {
    }
    var HTMLGlobalRolesElement: {
        prototype: HTMLGlobalRolesElement;
        new (): HTMLGlobalRolesElement;
    };
    interface HTMLTableRootElement extends Components.TableRoot, HTMLStencilElement {
    }
    var HTMLTableRootElement: {
        prototype: HTMLTableRootElement;
        new (): HTMLTableRootElement;
    };
    interface HTMLElementTagNameMap {
        "app-home": HTMLAppHomeElement;
        "app-profile": HTMLAppProfileElement;
        "app-root": HTMLAppRootElement;
        "cf-header": HTMLCfHeaderElement;
        "cf-header-menu": HTMLCfHeaderMenuElement;
        "cf-login": HTMLCfLoginElement;
        "cf-register": HTMLCfRegisterElement;
        "global-role-column-grants": HTMLGlobalRoleColumnGrantsElement;
        "global-role-memberships": HTMLGlobalRoleMembershipsElement;
        "global-role-table-permissions": HTMLGlobalRoleTablePermissionsElement;
        "global-roles": HTMLGlobalRolesElement;
        "table-root": HTMLTableRootElement;
    }
}
declare namespace LocalJSX {
    interface AppHome {
    }
    interface AppProfile {
        "match"?: MatchResults;
    }
    interface AppRoot {
        "history"?: RouterHistory;
    }
    interface CfHeader {
        "history"?: RouterHistory;
    }
    interface CfHeaderMenu {
        "history"?: RouterHistory;
        "onMenuClicked"?: (event: CustomEvent<MenuClickedEvent>) => void;
    }
    interface CfLogin {
        "history"?: RouterHistory;
    }
    interface CfRegister {
        "history"?: RouterHistory;
    }
    interface GlobalRoleColumnGrants {
    }
    interface GlobalRoleMemberships {
    }
    interface GlobalRoleTablePermissions {
    }
    interface GlobalRoles {
    }
    interface TableRoot {
        "match"?: MatchResults;
    }
    interface IntrinsicElements {
        "app-home": AppHome;
        "app-profile": AppProfile;
        "app-root": AppRoot;
        "cf-header": CfHeader;
        "cf-header-menu": CfHeaderMenu;
        "cf-login": CfLogin;
        "cf-register": CfRegister;
        "global-role-column-grants": GlobalRoleColumnGrants;
        "global-role-memberships": GlobalRoleMemberships;
        "global-role-table-permissions": GlobalRoleTablePermissions;
        "global-roles": GlobalRoles;
        "table-root": TableRoot;
    }
}
export { LocalJSX as JSX };
declare module "@stencil/core" {
    export namespace JSX {
        interface IntrinsicElements {
            "app-home": LocalJSX.AppHome & JSXBase.HTMLAttributes<HTMLAppHomeElement>;
            "app-profile": LocalJSX.AppProfile & JSXBase.HTMLAttributes<HTMLAppProfileElement>;
            "app-root": LocalJSX.AppRoot & JSXBase.HTMLAttributes<HTMLAppRootElement>;
            "cf-header": LocalJSX.CfHeader & JSXBase.HTMLAttributes<HTMLCfHeaderElement>;
            "cf-header-menu": LocalJSX.CfHeaderMenu & JSXBase.HTMLAttributes<HTMLCfHeaderMenuElement>;
            "cf-login": LocalJSX.CfLogin & JSXBase.HTMLAttributes<HTMLCfLoginElement>;
            "cf-register": LocalJSX.CfRegister & JSXBase.HTMLAttributes<HTMLCfRegisterElement>;
            "global-role-column-grants": LocalJSX.GlobalRoleColumnGrants & JSXBase.HTMLAttributes<HTMLGlobalRoleColumnGrantsElement>;
            "global-role-memberships": LocalJSX.GlobalRoleMemberships & JSXBase.HTMLAttributes<HTMLGlobalRoleMembershipsElement>;
            "global-role-table-permissions": LocalJSX.GlobalRoleTablePermissions & JSXBase.HTMLAttributes<HTMLGlobalRoleTablePermissionsElement>;
            "global-roles": LocalJSX.GlobalRoles & JSXBase.HTMLAttributes<HTMLGlobalRolesElement>;
            "table-root": LocalJSX.TableRoot & JSXBase.HTMLAttributes<HTMLTableRootElement>;
        }
    }
}
