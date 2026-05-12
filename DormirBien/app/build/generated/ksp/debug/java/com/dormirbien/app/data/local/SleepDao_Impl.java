package com.dormirbien.app.data.local;

import android.database.Cursor;
import android.os.CancellationSignal;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.room.CoroutinesRoom;
import androidx.room.EntityDeletionOrUpdateAdapter;
import androidx.room.EntityInsertionAdapter;
import androidx.room.EntityUpsertionAdapter;
import androidx.room.RoomDatabase;
import androidx.room.RoomSQLiteQuery;
import androidx.room.util.CursorUtil;
import androidx.room.util.DBUtil;
import androidx.sqlite.db.SupportSQLiteStatement;
import java.lang.Class;
import java.lang.Exception;
import java.lang.Object;
import java.lang.Override;
import java.lang.String;
import java.lang.SuppressWarnings;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Callable;
import javax.annotation.processing.Generated;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import kotlinx.coroutines.flow.Flow;

@Generated("androidx.room.RoomProcessor")
@SuppressWarnings({"unchecked", "deprecation"})
public final class SleepDao_Impl implements SleepDao {
  private final RoomDatabase __db;

  private final EntityUpsertionAdapter<SleepRecordEntity> __upsertionAdapterOfSleepRecordEntity;

  public SleepDao_Impl(@NonNull final RoomDatabase __db) {
    this.__db = __db;
    this.__upsertionAdapterOfSleepRecordEntity = new EntityUpsertionAdapter<SleepRecordEntity>(new EntityInsertionAdapter<SleepRecordEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "INSERT INTO `sleep_records` (`dateKey`,`hours`,`stars`,`feeling`) VALUES (?,?,?,?)";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SleepRecordEntity entity) {
        statement.bindString(1, entity.getDateKey());
        statement.bindDouble(2, entity.getHours());
        statement.bindLong(3, entity.getStars());
        statement.bindString(4, entity.getFeeling());
      }
    }, new EntityDeletionOrUpdateAdapter<SleepRecordEntity>(__db) {
      @Override
      @NonNull
      protected String createQuery() {
        return "UPDATE `sleep_records` SET `dateKey` = ?,`hours` = ?,`stars` = ?,`feeling` = ? WHERE `dateKey` = ?";
      }

      @Override
      protected void bind(@NonNull final SupportSQLiteStatement statement,
          @NonNull final SleepRecordEntity entity) {
        statement.bindString(1, entity.getDateKey());
        statement.bindDouble(2, entity.getHours());
        statement.bindLong(3, entity.getStars());
        statement.bindString(4, entity.getFeeling());
        statement.bindString(5, entity.getDateKey());
      }
    });
  }

  @Override
  public Object upsert(final SleepRecordEntity record,
      final Continuation<? super Unit> $completion) {
    return CoroutinesRoom.execute(__db, true, new Callable<Unit>() {
      @Override
      @NonNull
      public Unit call() throws Exception {
        __db.beginTransaction();
        try {
          __upsertionAdapterOfSleepRecordEntity.upsert(record);
          __db.setTransactionSuccessful();
          return Unit.INSTANCE;
        } finally {
          __db.endTransaction();
        }
      }
    }, $completion);
  }

  @Override
  public Flow<List<SleepRecordEntity>> observeAll() {
    final String _sql = "SELECT * FROM sleep_records ORDER BY dateKey DESC";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 0);
    return CoroutinesRoom.createFlow(__db, false, new String[] {"sleep_records"}, new Callable<List<SleepRecordEntity>>() {
      @Override
      @NonNull
      public List<SleepRecordEntity> call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDateKey = CursorUtil.getColumnIndexOrThrow(_cursor, "dateKey");
          final int _cursorIndexOfHours = CursorUtil.getColumnIndexOrThrow(_cursor, "hours");
          final int _cursorIndexOfStars = CursorUtil.getColumnIndexOrThrow(_cursor, "stars");
          final int _cursorIndexOfFeeling = CursorUtil.getColumnIndexOrThrow(_cursor, "feeling");
          final List<SleepRecordEntity> _result = new ArrayList<SleepRecordEntity>(_cursor.getCount());
          while (_cursor.moveToNext()) {
            final SleepRecordEntity _item;
            final String _tmpDateKey;
            _tmpDateKey = _cursor.getString(_cursorIndexOfDateKey);
            final float _tmpHours;
            _tmpHours = _cursor.getFloat(_cursorIndexOfHours);
            final int _tmpStars;
            _tmpStars = _cursor.getInt(_cursorIndexOfStars);
            final String _tmpFeeling;
            _tmpFeeling = _cursor.getString(_cursorIndexOfFeeling);
            _item = new SleepRecordEntity(_tmpDateKey,_tmpHours,_tmpStars,_tmpFeeling);
            _result.add(_item);
          }
          return _result;
        } finally {
          _cursor.close();
        }
      }

      @Override
      protected void finalize() {
        _statement.release();
      }
    });
  }

  @Override
  public Object getByKey(final String key,
      final Continuation<? super SleepRecordEntity> $completion) {
    final String _sql = "SELECT * FROM sleep_records WHERE dateKey = ? LIMIT 1";
    final RoomSQLiteQuery _statement = RoomSQLiteQuery.acquire(_sql, 1);
    int _argIndex = 1;
    _statement.bindString(_argIndex, key);
    final CancellationSignal _cancellationSignal = DBUtil.createCancellationSignal();
    return CoroutinesRoom.execute(__db, false, _cancellationSignal, new Callable<SleepRecordEntity>() {
      @Override
      @Nullable
      public SleepRecordEntity call() throws Exception {
        final Cursor _cursor = DBUtil.query(__db, _statement, false, null);
        try {
          final int _cursorIndexOfDateKey = CursorUtil.getColumnIndexOrThrow(_cursor, "dateKey");
          final int _cursorIndexOfHours = CursorUtil.getColumnIndexOrThrow(_cursor, "hours");
          final int _cursorIndexOfStars = CursorUtil.getColumnIndexOrThrow(_cursor, "stars");
          final int _cursorIndexOfFeeling = CursorUtil.getColumnIndexOrThrow(_cursor, "feeling");
          final SleepRecordEntity _result;
          if (_cursor.moveToFirst()) {
            final String _tmpDateKey;
            _tmpDateKey = _cursor.getString(_cursorIndexOfDateKey);
            final float _tmpHours;
            _tmpHours = _cursor.getFloat(_cursorIndexOfHours);
            final int _tmpStars;
            _tmpStars = _cursor.getInt(_cursorIndexOfStars);
            final String _tmpFeeling;
            _tmpFeeling = _cursor.getString(_cursorIndexOfFeeling);
            _result = new SleepRecordEntity(_tmpDateKey,_tmpHours,_tmpStars,_tmpFeeling);
          } else {
            _result = null;
          }
          return _result;
        } finally {
          _cursor.close();
          _statement.release();
        }
      }
    }, $completion);
  }

  @NonNull
  public static List<Class<?>> getRequiredConverters() {
    return Collections.emptyList();
  }
}
