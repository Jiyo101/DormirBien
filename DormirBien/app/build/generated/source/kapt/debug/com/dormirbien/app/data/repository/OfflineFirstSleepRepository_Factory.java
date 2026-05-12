package com.dormirbien.app.data.repository;

import com.dormirbien.app.data.local.SleepDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class OfflineFirstSleepRepository_Factory implements Factory<OfflineFirstSleepRepository> {
  private final Provider<SleepDao> daoProvider;

  public OfflineFirstSleepRepository_Factory(Provider<SleepDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public OfflineFirstSleepRepository get() {
    return newInstance(daoProvider.get());
  }

  public static OfflineFirstSleepRepository_Factory create(Provider<SleepDao> daoProvider) {
    return new OfflineFirstSleepRepository_Factory(daoProvider);
  }

  public static OfflineFirstSleepRepository newInstance(SleepDao dao) {
    return new OfflineFirstSleepRepository(dao);
  }
}
