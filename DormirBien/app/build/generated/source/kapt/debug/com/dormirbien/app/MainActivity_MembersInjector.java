package com.dormirbien.app;

import com.dormirbien.app.data.local.AlarmPreferences;
import com.dormirbien.app.data.repository.SleepRepository;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<AlarmPreferences> prefsProvider;

  private final Provider<SleepRepository> sleepRepoProvider;

  public MainActivity_MembersInjector(Provider<AlarmPreferences> prefsProvider,
      Provider<SleepRepository> sleepRepoProvider) {
    this.prefsProvider = prefsProvider;
    this.sleepRepoProvider = sleepRepoProvider;
  }

  public static MembersInjector<MainActivity> create(Provider<AlarmPreferences> prefsProvider,
      Provider<SleepRepository> sleepRepoProvider) {
    return new MainActivity_MembersInjector(prefsProvider, sleepRepoProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectPrefs(instance, prefsProvider.get());
    injectSleepRepo(instance, sleepRepoProvider.get());
  }

  @InjectedFieldSignature("com.dormirbien.app.MainActivity.prefs")
  public static void injectPrefs(MainActivity instance, AlarmPreferences prefs) {
    instance.prefs = prefs;
  }

  @InjectedFieldSignature("com.dormirbien.app.MainActivity.sleepRepo")
  public static void injectSleepRepo(MainActivity instance, SleepRepository sleepRepo) {
    instance.sleepRepo = sleepRepo;
  }
}
