package com.dormirbien.app.ui.home;

import com.dormirbien.app.data.local.AlarmPreferences;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava"
})
public final class HomeViewModel_Factory implements Factory<HomeViewModel> {
  private final Provider<AlarmPreferences> prefsProvider;

  public HomeViewModel_Factory(Provider<AlarmPreferences> prefsProvider) {
    this.prefsProvider = prefsProvider;
  }

  @Override
  public HomeViewModel get() {
    return newInstance(prefsProvider.get());
  }

  public static HomeViewModel_Factory create(Provider<AlarmPreferences> prefsProvider) {
    return new HomeViewModel_Factory(prefsProvider);
  }

  public static HomeViewModel newInstance(AlarmPreferences prefs) {
    return new HomeViewModel(prefs);
  }
}
