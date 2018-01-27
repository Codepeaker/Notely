package in.codepeaker.notely.data;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by github.com/codepeaker on 24/1/18.
 */

public class NotesData implements Parcelable {
    public static final Creator<NotesData> CREATOR = new Creator<NotesData>() {
        @Override
        public NotesData createFromParcel(Parcel in) {
            return new NotesData(in);
        }

        @Override
        public NotesData[] newArray(int size) {
            return new NotesData[size];
        }
    };
    private String notesTitle;
    private String notesDesc;
    private String notesLastUpdate;
    private long id;
    private boolean isStarred;
    private boolean isFav;
    private boolean isPoem;
    private boolean isPinned;
    private boolean isStory;

    protected NotesData(Parcel in) {
        notesTitle = in.readString();
        notesDesc = in.readString();
        notesLastUpdate = in.readString();
        id = in.readLong();
        isStarred = in.readByte() != 0;
        isFav = in.readByte() != 0;
        isPoem = in.readByte() != 0;
        isPinned = in.readByte() != 0;
        isStory = in.readByte() != 0;
    }

    public NotesData() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public boolean isPinned() {
        return isPinned;
    }

    public void setPinned(boolean pinned) {
        isPinned = pinned;
    }

    public String getNotesTitle() {
        return notesTitle;
    }

    public void setNotesTitle(String notesTitle) {
        this.notesTitle = notesTitle;
    }

    public String getNotesDesc() {
        return notesDesc;
    }

    public void setNotesDesc(String notesDesc) {
        this.notesDesc = notesDesc;
    }

    public String getNotesLastUpdate() {
        return notesLastUpdate;
    }

    public void setNotesLastUpdate(String notesLastUpdate) {
        this.notesLastUpdate = notesLastUpdate;
    }

    public boolean getIsStarred() {
        return isStarred;
    }

    public void setIsStarred(boolean isStarred) {
        this.isStarred = isStarred;
    }

    public boolean getIsFav() {
        return isFav;
    }

    public void setIsFav(boolean isFav) {
        this.isFav = isFav;
    }

    public boolean getIsPoem() {
        return isPoem;
    }

    public void setIsPoem(boolean isPoem) {
        this.isPoem = isPoem;
    }

    public boolean getIsStory() {
        return isStory;
    }

    public void setIsStory(boolean isStory) {
        this.isStory = isStory;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(notesTitle);
        dest.writeString(notesDesc);
        dest.writeString(notesLastUpdate);
        dest.writeLong(id);
        dest.writeByte((byte) (isStarred ? 1 : 0));
        dest.writeByte((byte) (isFav ? 1 : 0));
        dest.writeByte((byte) (isPoem ? 1 : 0));
        dest.writeByte((byte) (isPinned ? 1 : 0));
        dest.writeByte((byte) (isStory ? 1 : 0));
    }
}
