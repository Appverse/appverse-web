
package org.appverse.web.showcases.gwtshowcase.gwtfrontend.admin.users.presenters.interfaces;

import org.appverse.web.showcases.gwtshowcase.backend.model.presentation.UserVO;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.common.views.ValidatorView;
import org.appverse.web.showcases.gwtshowcase.gwtfrontend.common.views.toolbar.ToolbarView;

import com.google.gwt.user.client.ui.IsWidget;
import com.mvp4g.client.view.LazyView;
import com.mvp4g.client.view.ReverseViewInterface;

public interface UserEditView extends
		ReverseViewInterface<UserEditView.IUserEditPresenter>, LazyView,
		IsWidget, ToolbarView, ValidatorView<UserVO> {

	public interface IUserEditPresenter {

		public void cancel();

		public void delete(UserVO user);

		public void onUserEdit(final UserVO user);

		public void save(UserVO user);
	}

	@Override
	public void setEditMode(boolean editMode, boolean canDelete);

	// View methods here
	public void setUser(UserVO user);

}
