import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.dp

public val Icon_Chart: ImageVector
	get() {
		if (_undefined != null) {
			return _undefined!!
		}
		_undefined = ImageVector.Builder(
            name = "ChartBarSquare",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
			path(
    			fill = null,
    			fillAlpha = 1.0f,
    			stroke = SolidColor(Color(0xFF0F172A)),
    			strokeAlpha = 1.0f,
    			strokeLineWidth = 1.5f,
    			strokeLineCap = StrokeCap.Round,
    			strokeLineJoin = StrokeJoin.Round,
    			strokeLineMiter = 1.0f,
    			pathFillType = PathFillType.NonZero
			) {
				moveTo(7.5f, 14.25f)
				verticalLineTo(16.5f)
				moveTo(10.5f, 12f)
				verticalLineTo(16.5f)
				moveTo(13.5f, 9.75f)
				verticalLineTo(16.5f)
				moveTo(16.5f, 7.5f)
				verticalLineTo(16.5f)
				moveTo(6f, 20.25f)
				horizontalLineTo(18f)
				curveTo(19.2426f, 20.25f, 20.25f, 19.2426f, 20.25f, 18f)
				verticalLineTo(6f)
				curveTo(20.25f, 4.7574f, 19.2426f, 3.75f, 18f, 3.75f)
				horizontalLineTo(6f)
				curveTo(4.7574f, 3.75f, 3.75f, 4.7574f, 3.75f, 6f)
				verticalLineTo(18f)
				curveTo(3.75f, 19.2426f, 4.7574f, 20.25f, 6f, 20.25f)
				close()
			}
		}.build()
		return _undefined!!
	}

private var _undefined: ImageVector? = null
