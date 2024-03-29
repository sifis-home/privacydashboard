package com.privacydashboard.application.views;

import com.privacydashboard.application.data.GlobalVariables;
import com.privacydashboard.application.data.entity.User;
import com.privacydashboard.application.data.service.CommunicationService;
import com.privacydashboard.application.data.service.DataBaseService;
import com.privacydashboard.application.security.AuthenticatedUser;
import com.privacydashboard.application.security.UserDetailsServiceImpl;
import com.privacydashboard.application.views.apps.AppsView;
import com.privacydashboard.application.views.home.HomeView;
import com.privacydashboard.application.views.contacts.ContactsView;
import com.privacydashboard.application.views.messages.MessagesView;
import com.privacydashboard.application.views.mainLayout.NotificationView;
import com.privacydashboard.application.views.mainLayout.ProfileView;
import com.privacydashboard.application.views.privacyNotice.PrivacyNoticeView;
import com.privacydashboard.application.views.questionnaire.Questionnaire;
import com.privacydashboard.application.views.rights.ControllerDPORightsView;
import com.privacydashboard.application.views.rights.SubjectRightsView;
import com.privacydashboard.application.views.apps.AvailableAppsView;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.contextmenu.ContextMenu;
import com.vaadin.flow.component.dependency.NpmPackage;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Nav;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.html.UnorderedList;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.RouterLink;
import com.vaadin.flow.server.auth.AccessAnnotationChecker;

public class MainLayout extends AppLayout {

    public static class MenuItemInfo extends ListItem {

        private final Class<? extends Component> view;

        public MenuItemInfo(String menuTitle, String iconClass, Class<? extends Component> view) {
            this.view = view;
            RouterLink link = new RouterLink();
            link.addClassNames("menu-item-link");
            link.setRoute(view);

            Span text = new Span(menuTitle);
            text.addClassNames("menu-item-text");

            link.add(new LineAwesomeIcon(iconClass), text);
            add(link);
        }

        public Class<?> getView() {
            return view;
        }

        /**
         * Simple wrapper to create icons using LineAwesome iconset. See
         * https://icons8.com/line-awesome
         */
        @NpmPackage(value = "line-awesome", version = "1.3.0")
        public static class LineAwesomeIcon extends Span {
            public LineAwesomeIcon(String lineawesomeClassnames) {
                addClassNames("menu-item-icon");
                if (!lineawesomeClassnames.isEmpty()) {
                    addClassNames(lineawesomeClassnames);
                }
            }
        }
    }

    // the text will change based on the current page(afterNavigation())
    private H1 viewTitle;

    private final AuthenticatedUser authenticatedUser;
    private final AccessAnnotationChecker accessChecker;
    private final DataBaseService dataBaseService;
    private final CommunicationService communicationService;
    private final UserDetailsServiceImpl userDetailsService;

    public MainLayout(AuthenticatedUser authenticatedUser, AccessAnnotationChecker accessChecker, DataBaseService dataBaseService, CommunicationService communicationService, UserDetailsServiceImpl userDetailsService) {
        this.authenticatedUser = authenticatedUser;
        this.accessChecker = accessChecker;
        this.dataBaseService= dataBaseService;
        this.communicationService=communicationService;
        this.userDetailsService=userDetailsService;

        setPrimarySection(Section.DRAWER);
        addToNavbar(true, createHeaderContent());
        addToDrawer(createDrawerContent());
    }

    private Component createHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.addClassNames("view-toggle");
        toggle.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames("view-title");

        NotificationView notificationView=new NotificationView(authenticatedUser.getUser(), dataBaseService, communicationService);

        Header header = new Header(toggle, viewTitle, notificationView);
        header.addClassNames("view-header");
        return header;
    }

    private Component createDrawerContent() {
        H2 appName = new H2("Privacy Dashboard");
        appName.addClassNames("app-name");

        com.vaadin.flow.component.html.Section section = new com.vaadin.flow.component.html.Section(appName,
                createNavigation(), createFooter());
        section.addClassNames("drawer-section");
        return section;
    }

    private Nav createNavigation() {
        Nav nav = new Nav();
        nav.addClassNames("menu-item-container");
        nav.getElement().setAttribute("aria-labelledby", "views");

        // Wrap the links in a list; improves accessibility
        UnorderedList list = new UnorderedList();
        list.addClassNames("navigation-list");
        nav.add(list);

        for (MenuItemInfo menuItem : createMenuItems()) {
            if (accessChecker.hasAccess(menuItem.getView())) {
                list.add(menuItem);
            }

        }
        return nav;
    }

    private MenuItemInfo[] createMenuItems() {
        return new MenuItemInfo[]{ //
                new MenuItemInfo("Home", "las la-home", HomeView.class), //

                new MenuItemInfo("Contacts", "las la-address-book", ContactsView.class), //

                new MenuItemInfo("Messages", "las la-comments", MessagesView.class), //

                new MenuItemInfo("Rights", "las la-school", SubjectRightsView.class),  //ONLY FOR SUBJECTS

                new MenuItemInfo("Rights", "las la-school", ControllerDPORightsView.class),  //ONLY FOR CONTROLLERS AND DPOS

                new MenuItemInfo("Available Apps", "la la-list", AvailableAppsView.class), //

                new MenuItemInfo("Apps", "la la-list", AppsView.class), //

                new MenuItemInfo("PrivacyNotice", "la la-file", PrivacyNoticeView.class), //

                new MenuItemInfo("Questionnaire", "las la-archive", Questionnaire.class)

        };
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        layout.addClassNames("footer");

        User user=authenticatedUser.getUser();
        if (user!=null) {
            Avatar avatar = new Avatar(user.getName(), user.getProfilePictureUrl());
            avatar.addClassNames("me-xs");

            ContextMenu userMenu = new ContextMenu(layout);
            userMenu.setOpenOnClick(true);
            avatar.addClassNames("avatar");
            ProfileView profileView=new ProfileView(user, authenticatedUser, dataBaseService, userDetailsService);
            userMenu.addOpenedChangeListener(e->profileView.closePasswordDetail());
            userMenu.addOpenedChangeListener(e->profileView.closeMailDetail());
            userMenu.add(profileView);

            Span name = new Span(user.getName());
            name.addClassNames("font-medium", "text-s", "text-secondary", "footerName");
            layout.add(avatar, name);
        } else {
            Anchor loginLink = new Anchor("login", "Sign in");
            layout.add(loginLink);
        }

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? GlobalVariables.pageTitle : title.value();
    }
}
