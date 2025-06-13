package com.rafalskrzypczyk.ninetyvalues.presentation.new_entry

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.Color
import com.rafalskrzypczyk.ninetyvalues.R
import com.rafalskrzypczyk.ninetyvalues.ui.theme.ValuesEssential
import com.rafalskrzypczyk.ninetyvalues.ui.theme.ValuesImportant
import com.rafalskrzypczyk.ninetyvalues.ui.theme.ValuesIrrelevant
import com.rafalskrzypczyk.ninetyvalues.ui.theme.ValuesUnimportant
import com.rafalskrzypczyk.ninetyvalues.ui.theme.ValuesVeryImportant

enum class ValuesSelectingSteps (
    val level: Int,
    @StringRes val description: Int,
    val count: Int,
    val color: Color
) {
    ESSENTIAL(4,R.string.level_desc_essential, 10, ValuesEssential),
    VERY_IMPORTANT(3, R.string.level_desc_very_important, 15, ValuesVeryImportant),
    IMPORTANT(2, R.string.level_desc_important, 20, ValuesImportant),
    UNIMPORTANT(1, R.string.level_desc_unimportant, 20, ValuesUnimportant),
    IRRELEVANT(0, R.string.level_desc_irrelevant, 25, ValuesIrrelevant);

    companion object {
        fun getFirstStep() : ValuesSelectingSteps {
            return entries.first { it.level == 0 }
        }

        fun getNextStep(currentLevel: ValuesSelectingSteps) : ValuesSelectingSteps? {
            return entries.firstOrNull { it.level == currentLevel.level + 1 }
        }
    }
}