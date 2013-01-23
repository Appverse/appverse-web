package com.gft.storefront.gwtfrontend.main;

/*
 * GFT
 *
 * GWT project implemented using MVP4g showing the next features corresponding 
 * to the GWT front end architecture:
 * 
 * - Reverse MVP pattern (MVP4g implementation) 
 * - Dependency Injection (GIN implementation) 
 * - UiBinder (Plain GWT) 
 * - Lazy presenters and views (MVP4g implementation): presenters and views are not instantiated until they need to attend the first event 
 * - Command pattern: to show how to structure the application better, encapsulate reusable services, avoid large presenters, have a place 
 *   to implement caching, etc.. 
 * - History support (MVP4g implementation): back and forward browser buttons management, ability to "bookmark" a particular status of the 
 *   application, etc. 
 * - Cancellable Navigation (MVP4g implementation): ability to mark some transitions to ask for user confirmation (for instance in order 
 *   to prevent user to loss data because he is moving from one place ("screen") to another. 
 * - Editor framework (Plain GWT): Provides binding from POJO to GUI and the other way around 
 * - Code splitting (MVP4g implementation - using the concept of module, not splitters): This allows us not to load in the client browser 
 *   all the resources from the beginning. Instead, we specify the application to fetch them to the front end when they are requiered. 
 * - Layout templating (Plain GWT): simple example showing three areas: top area, bottom area and main (data area). Typically, the main area 
 *   is the one that is changing all the time to show the different "screens" 
 * - Other built-in patterns provided by MVP4G like singleton for presenters and views, etc. 
 * 
 * @author Miguel Fernandez Garrido
 */

import com.gft.storefront.gwtfrontend.main.main.history.GoodbyeHistoryConverter;
import com.gft.storefront.gwtfrontend.main.main.history.HelloHistoryConverter;
import com.gft.storefront.gwtfrontend.main.main.presenters.BottomPresenter;
import com.gft.storefront.gwtfrontend.main.main.presenters.ClientModuleLayoutManagerPresenter;
import com.gft.storefront.gwtfrontend.main.main.presenters.ExampleLayout2Presenter;
import com.gft.storefront.gwtfrontend.main.main.presenters.ExampleLayoutPresenter;
import com.gft.storefront.gwtfrontend.main.main.presenters.GoodbyePresenter;
import com.gft.storefront.gwtfrontend.main.main.presenters.HelloPresenter;
import com.gft.storefront.gwtfrontend.main.main.presenters.TopPresenter;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.mvp4g.client.annotation.Event;
import com.mvp4g.client.annotation.Events;
import com.mvp4g.client.annotation.InitHistory;
import com.mvp4g.client.annotation.NotFoundHistory;
import com.mvp4g.client.annotation.Start;
import com.mvp4g.client.event.EventBus;

@Events(startPresenter = ClientModuleLayoutManagerPresenter.class, historyOnStart = true)
public interface MainEventBus extends EventBus {

	@Event(handlers = { ClientModuleLayoutManagerPresenter.class })
	public void changeLayoutWidget(IsWidget layoutWidget);

	@Event(handlers = { ExampleLayout2Presenter.class })
	public void exampleLayout2ChangeBody(Widget bodyWidget);

	@Event(handlers = { ExampleLayout2Presenter.class, GoodbyePresenter.class })
	public void exampleLayout2Select();

	@Event(handlers = { ExampleLayoutPresenter.class })
	public void exampleLayoutChangeBody(Widget bodyWidget);

	@Event(handlers = { ExampleLayoutPresenter.class, HelloPresenter.class })
	public void exampleLayoutSelect();

	// @Event(handlers = PersonModifyPresenter.class, historyConverter =
	// PersonModifyHistoryConverter.class, navigationEvent = true)
	// public void modifyPerson(Person person);

	@NotFoundHistory
	@Event(handlers = HelloPresenter.class)
	public void notFound();

	@Event(handlers = GoodbyePresenter.class, historyConverter = GoodbyeHistoryConverter.class, navigationEvent = true)
	public void sayGoodbye(String goodbyeName);

	@Event(handlers = HelloPresenter.class, historyConverter = HelloHistoryConverter.class, navigationEvent = true)
	public void sayHello(String helloName);

	// @Event(handlers = PersonListPresenter.class, historyConverter =
	// PersonListHistoryConverter.class, navigationEvent = true)
	// public void showPersonList();

	@InitHistory
	@Start
	@Event(bind = { TopPresenter.class, BottomPresenter.class }, handlers = {
			ExampleLayoutPresenter.class, HelloPresenter.class })
	public void start();
}