package com.dormirbien.app.data.local;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AlarmPreferences_Factory implements Factory<AlarmPreferences> {
  private final Provider<Context> ctxProvider;

  public AlarmPreferences_Factory(Provider<Context> ctxProvider) {
    this.ctxProvider = ctxProvider;
  }

  @Override
  public AlarmPreferences get() {
    return newInstance(ctxProvider.get());
  }

  public static AlarmPreferences_Factory create(Provider<Context> ctxProvider) {
    return new AlarmPreferences_Factory(ctxProvider);
  }

  public static AlarmPreferences newInstance(Context ctx) {
    return new AlarmPreferences(ctx);
  }
}
