package model.export;

import model.HeightMap;

public interface HeightMapExporter {
    public void export(HeightMap heightMap, String filePath);
}
